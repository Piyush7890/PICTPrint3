package com.piyush.pictprint;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private List<String> title;
    public PagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> title) {
        super(fm);
        this.fragments = fragments;
        this.title = title;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }


}
