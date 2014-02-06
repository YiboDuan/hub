package com.tiriam.hubble.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IndexFragmentPagerAdapter extends FragmentPagerAdapter {
	
	static final int NUM_ITEMS = 3;
    public IndexFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
    	switch(i) {
    	case 0:
    		return new MenuFragment();
    	case 1:
    		return new FeedFragment();
    	case 2:
    		return new CreateHubFragment();
    	}
        return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
