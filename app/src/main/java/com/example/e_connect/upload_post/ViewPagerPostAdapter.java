package com.example.e_connect.upload_post;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerPostAdapter extends FragmentPagerAdapter {
    private Bundle dataBundle;

    public ViewPagerPostAdapter(@NonNull FragmentManager fm, Bundle bundle) {
        super(fm);
        this.dataBundle = bundle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            FeedPostFragment fragment = new FeedPostFragment();
            fragment.setArguments(dataBundle);
            return fragment;
        }
        else if (position == 1) {
            QueryPostFragment fragment = new QueryPostFragment();
            fragment.setArguments(dataBundle);
            return fragment;
        }
        else {
            NotesPostFragment fragment = new NotesPostFragment();
            fragment.setArguments(dataBundle);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Feed Post";
        }
        else if (position == 1) {
            return "Query Post";
        }
        else {
            return "Notes";
        }
    }
}
