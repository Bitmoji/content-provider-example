package com.bitstrips.example.contentproviderdemo.glide;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.Resource;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

/**
 * Holder of a Drawable for Glide. The Drawable may or may not Animatable. If it is,
 * we stop the animation once the resource is ready to be destroyed.
 *
 * This class is based off of {@link com.bumptech.glide.load.resource.drawable.NonOwnedDrawableResource}
 */
public class AnimatableDrawableResource implements Resource<Drawable> {

    private final Drawable mDrawable;

    public AnimatableDrawableResource(Drawable drawable) {
        mDrawable = drawable;
    }

    @NonNull
    @Override
    public Class<Drawable> getResourceClass() {
        return (Class<Drawable>) mDrawable.getClass();
    }

    @NonNull
    @Override
    public Drawable get() {
        return mDrawable;
    }

    /**
     * @see com.bumptech.glide.load.resource.drawable.NonOwnedDrawableResource
     */
    @Override
    public int getSize() {
        // 4 bytes per pixel for ARGB_8888 Bitmaps is something of a reasonable approximation. If
        // there are no intrinsic bounds, we can fall back just to 1.
        return Math.max(1, mDrawable.getIntrinsicWidth() * mDrawable.getIntrinsicHeight() * 4);
    }

    @Override
    public void recycle() {
        if (mDrawable instanceof Animatable) {
            ((Animatable) mDrawable).stop();
        }
    }
}
