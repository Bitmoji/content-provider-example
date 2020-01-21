package com.bitstrips.example.contentproviderdemo.glide;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Glide module for this sample app
 *
 * This module sets up our custom decoder classes to ensure that animated WebPs are
 * decoded correctly as animated images.
 */
@GlideModule
public class SampleGlideAppModule extends AppGlideModule {

    @Override
    public void registerComponents(
            @NonNull Context context,
            @NonNull Glide glide,
            @NonNull Registry registry) {
        registry
                // Register our decoding classes to ensure that animated webp decoding works
                .prepend(ByteBuffer.class, Drawable.class, new ByteBufferImageDecoderDrawableDecoder())
                .prepend(InputStream.class, Drawable.class, new InputStreamImageDecoderDrawableDecoder());
    }
}
