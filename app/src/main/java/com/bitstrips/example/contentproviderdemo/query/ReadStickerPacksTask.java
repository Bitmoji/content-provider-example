package com.bitstrips.example.contentproviderdemo.query;

import com.bitstrips.example.contentproviderdemo.model.StickerPack;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ReadStickerPacksTask extends AsyncTask<Void, Void, List<StickerPack>> {

    private final Context mContext;
    private final String mLocaleString;
    private final OnStickerPacksLoadedCallback mCallback;

    public ReadStickerPacksTask(Context context, String localeString, OnStickerPacksLoadedCallback callback) {
        mContext = context;
        mLocaleString = localeString;
        mCallback = callback;
    }

    @Override
    protected List<StickerPack> doInBackground(Void... voids) {
        return queryContentProviderForStickerPacks();
    }

    @Override
    protected void onPostExecute(List<StickerPack> stickerPacks) {
        mCallback.onStickerPacksLoaded(stickerPacks);
    }

    @NonNull
    private List<StickerPack> queryContentProviderForStickerPacks() {
        List<StickerPack> list = new ArrayList<>();

        Uri uri = QueryUris.stickerPacksUri(mLocaleString);

        Cursor cursor = mContext
                .getContentResolver()
                .query(uri, null, null, null, null);
        if (cursor == null) {
            return list;
        }

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            List<Uri> uris = queryContentProviderForStickers(id);
            list.add(new StickerPack(name, uris));
        }

        return list;
    }

    @NonNull
    private List<Uri> queryContentProviderForStickers(@NonNull String stickerPackId) {
        List<Uri> list = new ArrayList<>();

        Uri uri = QueryUris.stickerPackUri(stickerPackId, mLocaleString, true /*includeAnimated*/);

        Cursor cursor = mContext
                .getContentResolver()
                .query(uri, null, null, null, null);
        if (cursor == null) {
            return list;
        }

        while (cursor.moveToNext()) {
            list.add(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow("uri"))));
        }

        return list;
    }

    public interface OnStickerPacksLoadedCallback {
        void onStickerPacksLoaded(List<StickerPack> stickerPacks);
    }
}
