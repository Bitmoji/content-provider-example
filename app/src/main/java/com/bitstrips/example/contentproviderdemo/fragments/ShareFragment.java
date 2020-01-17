package com.bitstrips.example.contentproviderdemo.fragments;

import com.bitstrips.example.contentproviderdemo.R;
import com.bitstrips.example.contentproviderdemo.views.StickerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShareFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_fragment, container, false /*attachToRoot*/);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.share_button).setOnClickListener((v) -> {
            String smsPackage = Telephony.Sms.getDefaultSmsPackage(getContext());
            Uri stickerUri = view.<StickerView>findViewById(R.id.share_sticker_preview).getStickerUri();
            ContentValues values = new ContentValues();
            Context context = view.getContext();

            values.put("share_to", smsPackage);

            Uri shareableUri = context.getContentResolver().insert(stickerUri, values);

            if (shareableUri != null) {
                context.startActivity(new Intent(Intent.ACTION_SEND)
                        .setPackage(smsPackage)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setType("image/png")
                        .putExtra(Intent.EXTRA_STREAM, shareableUri));
            }
        });

        view.findViewById(R.id.share_animated_button).setOnClickListener((v) -> {
            String smsPackage = Telephony.Sms.getDefaultSmsPackage(getContext());
            Uri stickerUri = view.<StickerView>findViewById(R.id.share_animated_sticker_preview).getStickerUri();
            ContentValues values = new ContentValues();
            Context context = view.getContext();

            values.put("share_to", smsPackage);
            values.put("image_format", "gif");

            // Uncomment to add a white background to the image
            // values.put("with_white_background", true);

            Uri shareableUri = context.getContentResolver().insert(stickerUri, values);

            if (shareableUri != null) {
                context.startActivity(new Intent(Intent.ACTION_SEND)
                        .setPackage(smsPackage)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setType("image/gif")
                        .putExtra(Intent.EXTRA_STREAM, shareableUri));
            }
        });
    }
}
