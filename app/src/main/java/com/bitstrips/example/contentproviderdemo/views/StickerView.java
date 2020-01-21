package com.bitstrips.example.contentproviderdemo.views;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitstrips.example.contentproviderdemo.R;
import com.bitstrips.example.contentproviderdemo.executor.ExecutorUtils;
import com.bitstrips.example.contentproviderdemo.glide.GlideApp;

import java.io.IOException;

public class StickerView extends LinearLayout {

    private static final boolean USE_GLIDE = true;

    private ImageView mImageView;
    private TextView mTextView;
    private Uri mStickerUri;

    private AsyncTask mCurrentLoadTask;

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.sticker, this);
        mImageView = view.findViewById(R.id.sticker_image_view);
        mTextView = view.findViewById(R.id.sticker_text_view);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StickerView);
            String uri = typedArray.getString(R.styleable.StickerView_sticker_uri);
            if (uri != null) {
                setStickerUri(Uri.parse(uri));
            }
        }
    }

    public Uri getStickerUri() {
        return mStickerUri;
    }

    public void setStickerUri(@NonNull Uri uri) {
        if (USE_GLIDE) {
            GlideApp.with(getContext())
                    .asDrawable()
                    .load(uri)
                    .into(mImageView);
        } else {
            loadContentUri(uri);
        }

        mStickerUri = uri;
        mTextView.setText(uri.toString());
    }

    // Native Animated WebP decoding is only available as of Android P
    // Support for lower APIs requires an implementation with third-party libraries
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void loadContentUri(@NonNull Uri uri) {
        if (mCurrentLoadTask != null) {
            mCurrentLoadTask.cancel(true);
        }
        ImageDecoder.Source source = ImageDecoder.createSource(
                getContext().getContentResolver(), uri);

        mCurrentLoadTask = new DecodeImageAsyncTask().executeOnExecutor(
                ExecutorUtils.IMAGE_DECODING_EXECUTOR, source);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private class DecodeImageAsyncTask extends AsyncTask<ImageDecoder.Source, Void, Drawable> {

        @Override
        protected Drawable doInBackground(ImageDecoder.Source... sources) {
            try {
                return ImageDecoder.decodeDrawable(sources[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            mImageView.setImageDrawable(drawable);

            if (drawable instanceof Animatable2) {
                ((Animatable2) drawable).start();
            }
        }
    }
}
