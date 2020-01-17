# Bitmoji Content Providers guide
v0.5 2019-11-12

## Introduction

Sharing Bitmojis in Android has traditionally been a challenge due to the heterogeneous ecosystem of apps and sharing methods.

To make it easier for third parties to get Bitmoji content on their products, the Bitmoji app provides a standardized system to access and query its data in a straightforward fashion. The app exposes the data through the well known [Content Providers](https://developer.android.com/guide/topics/providers/content-providers), making it trivial for any app in the device to surface Bitmoji content without the need of account linkages or any other complex processes.

This document is accompanied by a demo application which showcases some of the most common scenarios to show and consume Bitmoji data.

## Preconditions

Bitmoji Android App must be installed.

A user with a Bitmoji Avatar must be signed into the app in order for successful Content Provider operation.

Developer Mode must be enabled to use the Content Provider for development purposes. To enable Developer Mode, enter the Settings screen and tap on the version string 4 times.

The Bitmoji Android App requires that enough disk storage is available to maintain image caches. Content Provider functionality requires at least 50MB of available disk storage.

A network connection with access to the internet is required to fetch images.

Your application must define the permission `com.bitstrips.imoji.provider.READ` in the app's `AndroidManifest.xml` file like so:

```diff
  <manifest ...>

+     <uses-permission android:name="com.bitstrips.imoji.provider.READ" />
```

## URI Schema

All the URIs must be prefixed with our content provider authority: **`com.bitstrips.imoji.provider`**

A list of all the available URIs is detailed next.

### Selfie

**URI:** `content://com.bitstrips.imoji.provider/me`

**Returns:** An image or *null* if there is no avatar available.

A selfie is a familiar representation of a user’s avatar. We recommend the usage of the selfie image as the call to action or button to open Bitmoji related activities.

This URI is usable directly to load content into an ImageView.

##### Example Code

```java
Glide.with(getContext())
    .load("content://com.bitstrips.imoji.provider/me")
    .into(mImageView);
```

### Selfie Search

**URI:** `content://com.bitstrips.imoji.provider/me/search?query={search_term}&locale={locale}`

**Query Parameters:**
 - `query`: The search term
 - `locale` (optional): A [language tag](https://en.wikipedia.org/wiki/IETF_language_tag) representing the locale that should be used for search. Only the language and country portions of the tag are supported. For example, the value `pt-BR` represents Brazilian Portuguese. If no value is specified, the locale of the device as set by the user will be used.

**Returns:** An image or *null* if there is no avatar available.

This method provides an easy way to create variations of the user selfie. A basic example could be changing the sentiment of the user in response to some event of the third party app implementing bitmojis. Selfies that respond to terms such as "happy", “sad” or “tired” could provide variations of the selfie creating a dynamic behaviour. If there is no match for the *search_term* provided, the default selfie is returned.

##### Example Code

```java
Glide.with(getContext())
    .load("content://com.bitstrips.imoji.provider/me?search=happy")
    .into(mImageView);
```

### Sticker

**URI:** `content://com.bitstrips.imoji.provider/me/sticker/{sticker_id}`

**Returns:** An image or *null* if there is no avatar available.

This is the generic URI that describes a Bitmoji sticker. The previous *Selfie* cases are a convenience built on top of regular stickers. The example code to use a sticker with Glide (or Picasso) is:

##### Example Code

```java
String sticker = "content://com.bitstrips.imoji.provider/me/sticker/d044e268-5b7b-4795-92d3-c82609f85f55";
Glide.with(getContext())
    .load(sticker)
    .into(mImageView);
```

The challenge here is to obtain that *sticker_id.* The rest of the schema explains how to obtain lists of stickers with packs and search.

### Sticker Packs

Bitmoji Stickers are grouped in *packs* that allow for easier classification. It is easy to build a tab/tag based interface with these packs. This kind of interface is really convenient for users who recently have installed Bitmoji. It allows them to browse the collection and get acquainted with the stickers available.

#### Getting all the packs available

**URI:** `content://com.bitstrips.imoji.provider/packs?locale={locale}`

**Query Parameters:**
 - `locale` (optional): A [language tag](https://en.wikipedia.org/wiki/IETF_language_tag) representing the locale that should be used for the sticker packs. Only the language and country portions of the tag are supported. For example, the value `pt-BR` represents Brazilian Portuguese. If no value is specified, the locale of the device as set by the user will be used.

**Returns:** A list of sticker packs defined by their properties `id` and `name`.

##### Example Code

```java
Uri uri = new Uri.Builder()
       .scheme(ContentResolver.SCHEME_CONTENT)
       .authority("com.bitstrips.imoji.provider")
       .appendPath("packs")
       .build();

Cursor cursor = getContext()
       .getContentResolver()
       .query(uri, null, null, null, null);
       
if (cursor == null) {
   // Handle this case
}

while (cursor.moveToNext()) {
   String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
   String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));

   // Do something with `id` and `name` here
}
```

#### Getting all the stickers for a given pack

**URI:** `content://com.bitstrips.imoji.provider/pack/{pack_id}?locale={locale}&include_animated={include_animated}`

**Query Parameters:**
 - `locale` (optional): A [language tag](https://en.wikipedia.org/wiki/IETF_language_tag) representing the locale that should be used for the sticker packs. Only the language and country portions of the tag are supported. For example, the value `pt-BR` represents Brazilian Portuguese. If no value is specified, the locale of the device as set by the user will be used.
 - `include_animated` (optional): A boolean for whether or not to include animated stickers (false by default)

**Returns:** A list of stickers defined by their `uri` property.

To perform this action we will need the `pack_id` for a given pack from the previous step. This will be added to the URI `content://com.bitstrips.imoji.provider/pack/{pack_id}`. For example, given a pack with `id` **popular** the URI would be `content://com.bitstrips.imoji.provider/pack/popular`.

##### Example Code

```java
List<Uri> list = new ArrayList<>();

Uri uri = new Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT)
        .authority("com.bitstrips.imoji.provider")
        .appendPath("pack")
        .appendPath(stickerPackId)
        .build();

Cursor cursor = getContext()
        .getContentResolver()
        .query(uri, null, null, null, null);
if (cursor == null) {
    return list;
}

while (cursor.moveToNext()) {
    list.add(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow("uri"))));
}

return list;
```

### Search

**URI:** `content://com.bitstrips.imoji.provider/search?query={search_term}&locale={locale}&include_animated={include_animated}`

**Query Parameters:**
 - `query`: The search term
 - `locale` (optional): A [language tag](https://en.wikipedia.org/wiki/IETF_language_tag) representing the locale that should be used for search. Only the language and country portions of the tag are supported. For example, the value `pt-BR` represents Brazilian Portuguese. If no value is specified, the locale of the device as set by the user will be used.
 - `include_animated` (optional): A boolean for whether or not to include animated stickers (false by default)

**Returns:** A list of stickers defined by their `uri` property.

Given the size of the Bitmoji collection, a search functionality is provided. This feature is key for users with some experience with Bitmoji. It allows them to quickly find the right well-known sticker they want to share. It also allows for discovery of new ways of sharing a given sentiment. On top of classic explicit search, this URI can also be used to create an autosuggest experience.

For example, querying `content://com.bitstrips.imoji.provider/search?query=hello` would return the cursor to the list of URIs matching the term *hello*. Each element contains a single field named `uri`.

##### Example Code

```shell
$ adb shell content query --uri content://com.bitstrips.imoji.provider/search?query=hello
Row: 0 uri=content://com.bitstrips.imoji.provider/me/sticker/d044e268-5b7b-4795-92d3-c82609f85f55, text=NULL, is_animated=0
Row: 1 uri=content://com.bitstrips.imoji.provider/me/sticker/1eb63610-73fb-4bb3-9e5a-775ba90c8563, text=NULL, is_animated=0
```

### Tags

Stickers within the Bitmoji collection are labeled with tags. Functionality is provided for searching these tags and for retrieving the stickers matching a specific tag. Together these support an experience where users would first see a list of autosuggest tags related to what they've typed, then select one of the suggested tags, and finally see the corresponding stickers.

#### Tag Search

**URI:** `content://com.bitstrips.imoji.provider/search/tags?query={search_term}&locale={locale}`

**Query Parameters:**
 - `query`: The search term
 - `locale` (optional): A [language tag](https://en.wikipedia.org/wiki/IETF_language_tag) representing the locale that should be used for search. Only the language and country portions of the tag are supported. For example, the value `pt-BR` represents Brazilian Portuguese. If no value is specified, the locale of the device as set by the user will be used.

**Returns:** A list of relevant tags found on stickers in the Bitmoji collection.

For example, querying `content://com.bitstrips.imoji.provider/search/tags?query=mon` would return the cursor to the list of tags matching the term *mon*. The results would be *("money", "monday", ...)*. This could be used as the first stage of an autosuggest experience.

Note that while many relevant tags will be prefixed by the search term, others will not.

Each element contains a single field named `tag`.

##### Example Code

```shell
$ adb shell content query --uri content://com.bitstrips.imoji.provider/search/tags?query=lun
Row: 0 tag=lunch
Row: 1 tag=lunch time
Row: 2 tag=lunar new year
...

$ adb shell content query --uri content://com.bitstrips.imoji.provider/search/tags?query=suns
Row: 0 tag=sunshine
Row: 1 tag=hello sunshine
```

#### Tagged Sticker Retrieval

**URI:** `content://com.bitstrips.imoji.provider/tags/{tag}/stickers?locale={locale}&include_animated={include_animated}`

**Query Parameters:**
 - `locale` (optional): A [language tag](https://en.wikipedia.org/wiki/IETF_language_tag) representing the locale that should be used for search. Only the language and country portions of the tag are supported. For example, the value `pt-BR` represents Brazilian Portuguese. If no value is specified, the locale of the device as set by the user will be used.
 - `include_animated` (optional): A boolean for whether or not to include animated stickers (false by default)

**Returns:** A list of stickers defined by their `uri` property.

This is a "get by tag" feature, not a search feature, so it returns only *exact* matches for `{tag}`. This could be used as the second stage of an autosuggest experience, to retrieve stickers when the user selects one of the autosuggested items.

For example, querying `content://com.bitstrips.imoji.provider/tags/have%20a%20nice%20day/stickers` would return the cursor to the list of stickers tagged with the exact string *have a nice day*.

Every element contains a field named `uri`. A field named `text` will contain alt-text for some stickers, otherwise `NULL`.

##### Example Code

```shell
$ adb shell content query --uri content://com.bitstrips.imoji.provider/tags/snow/stickers
Row: 0 uri=content://com.bitstrips.imoji.provider/me/sticker/479f75b9-fd05-4f19-8fa5-8fb788a5b808, text=NULL, is_animated=0
Row: 1 uri=content://com.bitstrips.imoji.provider/me/sticker/e596af3d-3d39-45e3-b3e7-2c8bcc235db1, text=NULL, is_animated=0
Row: 2 uri=content://com.bitstrips.imoji.provider/me/sticker/016be1c2-8190-4e5c-b8b1-9b71b912c9f4, text=NULL, is_animated=0
```

### Status

**URI:** `content://com.bitstrips.imoji.provider/status`

**Returns:** The state of content provider with respect to the calling app defined by their `status` property.

Value|Description
-----|-----------
`NO_ACCESS`|The calling app has not been granted access to the content provider. See `Requesting Access`.
`NO_AVATAR`|The calling app has been granted access, there is no Bitmoji Avatar available. The user must open the Bitmoji app to login and/or create an Avatar.
`READY`|The content provider is ready for use.

This URI can be used determine whether the app has access to the content provider. Depending on the return value the app can take appropriate actions to establish access to the content content provider.

##### Example Code

```java
Uri uri = new Uri.Builder()
       .scheme(ContentResolver.SCHEME_CONTENT)
       .authority("com.bitstrips.imoji.provider")
       .appendPath("status")
       .build();

Cursor cursor = getContext()
       .getContentResolver()
       .query(uri, null, null, null, null);
       
if (cursor == null || !cursor.moveToNext()) {
   // Update or install the Bitmoji App
}

String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

// Do something with `status` here
```

##### Requesting Access

To request access to the content provider the app can launch an intent which is handled by the Bitmoji app. In this process the user will be presented with a dialog asking for their consent for content provider access.

```java
private void requestAccess() {
    startActivityForResult(
          new Intent(Intent.ACTION_VIEW)
              .setData(Uri.parse("imoji://content-provider/connected-apps")), 1);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == 1) {
        if (resultCode == RESULT_OK) {
            // The request for access to the content provider has been granted
        } else {
            // The request for access to the content provider has been rejected
        }
    }
}
```

## Sharing

To share stickers to other apps, the app must use the `share_to` method. This allows the other app to access an individual sticker URI without requiring that app to have access to the entire Bitmoji app content provider.

Only sticker URIs can be shared.

**URI:** `content://com.bitstrips.imoji.provider/me/sticker/{sticker_id}`

**Content Values:**
 - `share_to`: The package name of the app that will receive the shared sticker
 - `image_format` (optional): The image format used to share the image. Acceptable values are `png` and `webp` for regular stickers, `gif` and `webp` for animated stickers
 - `with_white_background` (optional): A boolean to determine whether the shared image should have a white background (false by default)

**Returns:** The URI to be shared with the other app or *null* if an error occurs.

##### Example Code

```java
ContentValues values = new ContentValues();
values.put("share_to", "com.example.app");
values.put("image_format", "png");
values.put("with_white_background", "false");

Uri shareableUri = context.getContentResolver().insert(stickerUri, values);
```

## Notifications

It is common for users to update or change their Avatar within the Bitmoji app. A subscription to notifications on the Selfie URI enables the app to recieve notifications for Avatar changes. Notifications will be sent whenever the Avatar or sticker content has changed, including signing into and logging out of the Bitmoji App. 

The notification is only sent to the Selfie URI, but implies that content for all sticker URIs has changed.

##### Example Code

```java
ContentObserver contentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
    @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            // Do something here
        }
};

Uri selfieUri = Uri.parse("content://com.bitstrips.imoji.provider/me");

getContentResolver().registerContentObserver(selfieUri, contentObserver);
```

## Visual Identity

The Bitmoji Visual Identity library provides a set of images for common use cases.

### Query Visual Identity Library

The entries in the Visual Identity library can be retrieved by querying and images can be accessed using human readable names. The contents of the library can be specialized for each developer by contacting the Bitmoji team.

**URI:** `content://com.bitstrips.imoji.provider/me/library`

**Returns:** A list of Visual Identity library entry names.

### Open Visual Identity Library Entry

Each entry in the Visual Identity library can be access directly.

**URI:** `content://com.bitstrips.imoji.provider/me/library/{name}`

**Returns:** An image or *null* if an error occurs.
