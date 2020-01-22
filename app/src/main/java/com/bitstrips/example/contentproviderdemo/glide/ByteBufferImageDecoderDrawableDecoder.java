package com.bitstrips.example.contentproviderdemo.glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.DefaultImageHeaderParser;

import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Decodes a image byte buffer into a {@link Drawable}, using Android's native {@link ImageDecoder}
 */
@RequiresApi(28)
public class ByteBufferImageDecoderDrawableDecoder implements ResourceDecoder<ByteBuffer, Drawable> {

    private final DrawableImageDecoderResourceDecoder mDecoder = new DrawableImageDecoderResourceDecoder();

    @Override
    public boolean handles(
            @NonNull ByteBuffer source, @NonNull Options options) throws IOException {
        ImageHeaderParser.ImageType type = ImageHeaderParserUtils.getType(
                Arrays.asList(new DefaultImageHeaderParser()),
                source);

        // Only use this parser for WebP images
        return (type == ImageHeaderParser.ImageType.WEBP) || (type == ImageHeaderParser.ImageType.WEBP_A);
    }

    @Nullable
    @Override
    public Resource<Drawable> decode(
            @NonNull ByteBuffer source, int width, int height, @NonNull Options options)
            throws IOException {
        return mDecoder.decode(
                ImageDecoder.createSource(source),
                width,
                height,
                options);
    }
}
