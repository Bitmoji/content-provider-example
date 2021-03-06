package com.bitstrips.example.contentproviderdemo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bitstrips.example.contentproviderdemo.R;
import com.bitstrips.example.contentproviderdemo.adpater.StickersAdapter;
import com.bitstrips.example.contentproviderdemo.model.Language;
import com.bitstrips.example.contentproviderdemo.query.SearchTask;

public class SearchFragment extends Fragment {

    private static final int COLUMN_COUNT = 3;

    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private StickersAdapter mAdapter;
    private Spinner mSpinner;

    private String mLocaleString = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        mEditText = view.findViewById(R.id.search_text);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(v.getText().toString());
                    return true;
                }
                return false;
            }
        });

        mAdapter = new StickersAdapter();
        mRecyclerView = view.findViewById(R.id.search_results_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMN_COUNT));
        mRecyclerView.setAdapter(mAdapter);

        mSpinner = view.findViewById(R.id.language_spinner);
        mSpinner.setAdapter(Language.asArrayAdapter(getContext()));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLocaleString = Language.values()[position].getLocaleString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void search(@NonNull String text) {
        new SearchTask(getContext(), text, mLocaleString, mAdapter::setImageUris).execute();
    }
}
