package com.bitstrips.example.contentproviderdemo.adpater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bitstrips.example.contentproviderdemo.R;
import com.bitstrips.example.contentproviderdemo.model.StickerPack;
import com.bitstrips.example.contentproviderdemo.viewholder.PackViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PacksAdapter extends RecyclerView.Adapter<PackViewHolder> {

    private List<StickerPack> mPacks = new ArrayList<>();

    @NonNull
    @Override
    public PackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sticker_packs_list_entry, viewGroup, false);
        return new PackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackViewHolder viewHolder, int i) {
        viewHolder.bind(mPacks.get(i));
    }

    @Override
    public int getItemCount() {
        return mPacks.size();
    }

    public void setPacks(@NonNull List<StickerPack> packs) {
        mPacks = packs;
        notifyDataSetChanged();
    }
}
