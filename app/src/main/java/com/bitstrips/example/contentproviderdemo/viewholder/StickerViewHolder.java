package com.bitstrips.example.contentproviderdemo.viewholder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bitstrips.example.contentproviderdemo.R;
import com.bitstrips.example.contentproviderdemo.views.StickerView;

import android.net.Uri;
import android.view.View;

public class StickerViewHolder extends RecyclerView.ViewHolder {
    private final StickerView mStickerView;

    public StickerViewHolder(@NonNull View view) {
        super(view);
        mStickerView = view.findViewById(R.id.search_results_entry_sticker);
    }

    public void bind(Uri imageUri) {
        mStickerView.setStickerUri(imageUri);
    }
}
