package com.bitstrips.example.contentproviderdemo.glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;

import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decodes an {@link InputStream} into a {@link Drawable}, using Android's native {@link ImageDecoder}
 */
@RequiresApi(28)
public class InputStreamImageDecoderDrawableDecoder implements ResourceDecoder<InputStream, Drawable> {

    private final DrawableImageDecoderResourceDecoder mDecoder = new DrawableImageDecoderResourceDecoder();

    @Override
    public boolean handles(
            @NonNull InputStream source, @NonNull Options options) {
        return true;
    }

    @Nullable
    @Override
    public Resource<Drawable> decode(
            @NonNull InputStream source, int width, int height, @NonNull Options options)
            throws IOException {
        return mDecoder.decode(
                ImageDecoder.createSource(ByteBufferUtil.fromStream(source)),
                width,
                height,
                options);
    }
}
