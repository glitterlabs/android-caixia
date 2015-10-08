package com.glitterlabs.caixia.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.glitterlabs.caixia.Fragments.CameraFragmentFirst;
import com.glitterlabs.caixia.Fragments.InboxFragment;


/*
    Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
    All rights reserved. Patents pending.

    Responsible: Abhay Bhusari
 */
public class PagerAdapter  extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return CameraFragmentFirst.newInstance(position);
            case 1: // Fragment # 0 - This will show SecondFragment
                return InboxFragment.newInstance(position);
            /*case 2: // Fragment # 0 - This will show SecondFragment
                return CameraFragmentFirst.newInstance(position);*/


            default:
                return null;
        }


    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
