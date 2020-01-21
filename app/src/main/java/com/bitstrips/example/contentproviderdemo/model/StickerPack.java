package com.bitstrips.example.contentproviderdemo.model;

import androidx.annotation.NonNull;

import android.net.Uri;

import java.util.List;

public class StickerPack {
    public String mName;
    public List<Uri> mImageUris;

    public StickerPack(@NonNull String name, @NonNull List<Uri> imageUris) {
        mName = name;
        mImageUris = imageUris;
    }
}
