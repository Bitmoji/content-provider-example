package com.bitstrips.example.contentproviderdemo.glide;

import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.ImageDecoderResourceDecoder;

import android.graphics.ImageDecoder;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import java.io.IOException;

/**
 * Uses Android's native {@link ImageDecoder} class to decode images into {@link Drawable}s.
 * This class is able to decode animated image types, as opposed to static images provided
 * by Glide's default image decoding.
 */
@RequiresApi(28)
public class DrawableImageDecoderResourceDecoder extends ImageDecoderResourceDecoder<Drawable> {

    @Override
    protected Resource<Drawable> decode(
            ImageDecoder.Source source,
            int requestedWidth,
            int requestedHeight,
            ImageDecoder.OnHeaderDecodedListener listener) throws IOException {
        Drawable drawable = ImageDecoder.decodeDrawable(source, listener);

        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }

        return new AnimatableDrawableResource(drawable);
    }
}
