package com.bitstrips.example.contentproviderdemo.glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.resource.bitmap.DefaultImageHeaderParser;
import com.bumptech.glide.util.ByteBufferUtil;

import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Decodes an {@link InputStream} into a {@link Drawable}, using Android's native {@link ImageDecoder}
 */
@RequiresApi(28)
public class InputStreamImageDecoderDrawableDecoder implements ResourceDecoder<InputStream, Drawable> {

    private final int HEADER_SIZE_LIMIT = 5 * 1024 * 1024;

    private final DrawableImageDecoderResourceDecoder mDecoder = new DrawableImageDecoderResourceDecoder();
    private final ArrayPool mArrayPool = new LruArrayPool(HEADER_SIZE_LIMIT);

    @Override
    public boolean handles(
            @NonNull InputStream source, @NonNull Options options) throws IOException {
        ImageHeaderParser.ImageType type = ImageHeaderParserUtils.getType(
                Arrays.asList(new DefaultImageHeaderParser()),
                source,
                mArrayPool);

        // Only use this parser for WebP images
        return (type == ImageHeaderParser.ImageType.WEBP) || (type == ImageHeaderParser.ImageType.WEBP_A);
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
