package com.bitstrips.example.contentproviderdemo.query;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SearchTask extends AsyncTask<Void, Void, List<Uri>> {

    private final Context mContext;
    private final String mQuery;
    private final String mLocaleString;
    private final OnSearchCompleteCallback mCallback;

    public SearchTask(Context context, String query, String localeString, OnSearchCompleteCallback callback) {
        mContext = context;
        mQuery = query;
        mLocaleString = localeString;
        mCallback = callback;
    }

    @Override
    protected List<Uri> doInBackground(Void... voids) {
        return queryContentProvider(mQuery);
    }

    @Override
    protected void onPostExecute(List<Uri> uris) {
        mCallback.onSearchComplete(uris);
    }

    @NonNull
    private List<Uri> queryContentProvider(@NonNull String text) {
        List<Uri> list = new ArrayList<>();

        Uri uri = QueryUris.searchUri(text, mLocaleString, true /*includeAnimated*/);

        Cursor cursor = mContext.getContentResolver()
                .query(uri, null, null, null, null);

        if (cursor == null) {
            return list;
        }

        while (cursor.moveToNext()) {
            list.add(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow("uri"))));
        }

        return list;
    }

    public interface OnSearchCompleteCallback {
        void onSearchComplete(List<Uri> uris);
    }
}
