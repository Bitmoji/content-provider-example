package com.bitstrips.example.contentproviderdemo;

import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.bitstrips.example.contentproviderdemo.fragments.SearchFragment;
import com.bitstrips.example.contentproviderdemo.fragments.SelfieFragment;
import com.bitstrips.example.contentproviderdemo.fragments.ShareFragment;
import com.bitstrips.example.contentproviderdemo.fragments.StickerPacksFragment;
import com.bitstrips.example.contentproviderdemo.query.QueryUris;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {


    private static final List<Class<? extends Fragment>> FRAGMENTS = Arrays.asList(
            SelfieFragment.class,
            ShareFragment.class,
            StickerPacksFragment.class,
            SearchFragment.class);

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ContentObserver mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                mPagerAdapter.notifyDataSetChanged();
            }
        };

        getContentResolver().registerContentObserver(QueryUris.me(), false, mObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }

    private class MainActivityPagerAdapter extends FragmentStatePagerAdapter {

        public MainActivityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return Fragment.instantiate(MainActivity.this, FRAGMENTS.get(i).getName());
        }

        @Override
        public int getCount() {
            return FRAGMENTS.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }
}
