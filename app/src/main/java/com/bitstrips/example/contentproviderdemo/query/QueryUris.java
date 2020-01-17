package com.bitstrips.example.contentproviderdemo.query;

import android.content.ContentResolver;
import android.net.Uri;

public class QueryUris {

    public static Uri stickerPackUri(String packId, String localeString, boolean includeAnimated) {
        Uri.Builder builder = newBuilder()
                .appendPath("pack")
                .appendPath(packId)
                .appendQueryParameter("include_animated", String.valueOf(includeAnimated));

        if (localeString != null) {
            builder.appendQueryParameter("locale", localeString);
        }
        return builder.build();
    }

    public static Uri stickerPacksUri(String localeString) {
        Uri.Builder builder = newBuilder()
                .appendPath("packs");

        if (localeString != null) {
            builder.appendQueryParameter("locale", localeString);
        }
        return builder.build();
    }

    public static Uri searchUri(String query, String localeString, boolean includeAnimated) {
        Uri.Builder builder = newBuilder()
                .appendPath("search")
                .appendQueryParameter("query", query)
                .appendQueryParameter("include_animated", String.valueOf(includeAnimated));

        if (localeString != null) {
            builder.appendQueryParameter("locale", localeString);
        }
        return builder.build();
    }

    public static Uri me() {
        return newBuilder()
                .appendPath("me")
                .build();
    }

    public static Uri status() {
        return newBuilder()
                .appendPath("status")
                .build();
    }

    private static Uri.Builder newBuilder() {
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority("com.bitstrips.imoji.provider");
    }
}
