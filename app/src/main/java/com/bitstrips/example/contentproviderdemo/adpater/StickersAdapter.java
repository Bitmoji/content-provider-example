package com.bitstrips.example.contentproviderdemo.adpater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bitstrips.example.contentproviderdemo.R;
import com.bitstrips.example.contentproviderdemo.viewholder.StickerViewHolder;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class StickersAdapter extends RecyclerView.Adapter<StickerViewHolder> {

    private List<Uri> mImageUris = new ArrayList<>();

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_results_list_entry, viewGroup, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder viewHolder, int i) {
        viewHolder.bind(mImageUris.get(i));
    }

    @Override
    public int getItemCount() {
        return mImageUris.size();
    }

    public void setImageUris(@NonNull List<Uri> imageUris) {
        mImageUris = imageUris;
        notifyDataSetChanged();
    }
}
