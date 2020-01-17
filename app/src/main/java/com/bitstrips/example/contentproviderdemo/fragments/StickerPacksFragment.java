package com.bitstrips.example.contentproviderdemo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.bitstrips.example.contentproviderdemo.R;
import com.bitstrips.example.contentproviderdemo.adpater.PacksAdapter;
import com.bitstrips.example.contentproviderdemo.model.Language;
import com.bitstrips.example.contentproviderdemo.query.ReadStickerPacksTask;

public class StickerPacksFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PacksAdapter mAdapter;
    private Spinner mSpinner;
    private AsyncTask mAsyncTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sticker_packs_fragment, container, false);

        mAdapter = new PacksAdapter();
        mRecyclerView = view.findViewById(R.id.sticker_packs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAsyncTask = new ReadStickerPacksTask(getContext(), null, mAdapter::setPacks).execute();

        mSpinner = view.findViewById(R.id.language_spinner);
        mSpinner.setAdapter(Language.asArrayAdapter(getContext()));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAsyncTask.cancel(true);
                mAsyncTask = new ReadStickerPacksTask(
                        getContext(), Language.values()[position].getLocaleString(), mAdapter::setPacks).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAsyncTask.cancel(true);
        mAsyncTask = null;
    }
}
