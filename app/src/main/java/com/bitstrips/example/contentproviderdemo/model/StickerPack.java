package com.bitstrips.example.contentproviderdemo.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.List;

public class StickerPack {
    public String mName;
    public List<Uri> mImageUris;

    public StickerPack(@NonNull String name, @NonNull List<Uri> imageUris) {
        mName = name;
        mImageUris = imageUris;
    }
}
