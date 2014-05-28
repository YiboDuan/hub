package com.tiriam.hubble.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public class IndexFragmentPagerAdapter extends FragmentPagerAdapter {
	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
	static final int NUM_ITEMS = 4;
	boolean isChatActivated = false;
    public IndexFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
    	switch(position) {
    	case 0:
    		return "Menu";
    	case 1:
    		return "Active Hubs";
    	case 2:
    		//return ((ChatFragment)registeredFragments.get(2)).getPageTitle();
    		return "";
    	case 3:
    		return "Create Hub";
    	default:
    		return "";
    	}
    }
    
    @Override
    public Fragment getItem(int i) {
    	switch(i) {
    	case 0:
    		return new MenuFragment();
    	case 1:
    		return new FeedFragment();
    	case 2:
    		return ChatFragment.newInstance(null);
    	case 3:
    		return new CreateHubFragment();
    	}
        return null;
    }
    
    @Override
    public float getPageWidth(int position) {
	    if (position == 0) {
	        return(0.8f);
	    } else {
	        return (1.0f);       
	    }
    }
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}