package com.test.ksports.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by kingwag on 2016/12/4.
 * 页面切换适配器
 */

public class TabAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    List<String> strings;
    public TabAdapter(FragmentManager fm, List<Fragment> fragments, List<String> strings) {
        super(fm);
        this.fragments = fragments;
        this.strings = strings;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position);
    }
}
