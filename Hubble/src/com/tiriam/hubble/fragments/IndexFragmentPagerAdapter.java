package com.tiriam.hubble.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.ViewGroup;

public class IndexFragmentPagerAdapter extends FragmentPagerAdapter {
	private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private List<AtomicBoolean> flags = new ArrayList<AtomicBoolean>();
	Context context;
	
    public IndexFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public int getItemPosition (Object object)
    {
      int index = registeredFragments.indexOfValue((Fragment) object);
      if (index == -1)
        return POSITION_NONE;
      else
        return index;
    }
    
    @Override
    public Fragment getItem(int i) {
//    	switch(i) {
//    	case 0:
//    		return new MenuFragment();
//    	case 1:
//    		return new FeedFragment();
//    	case 2:
//    		return new ChatFragment();
//    	case 3:
//    		return new CreateHubFragment();
//    	}
        return registeredFragments.get(i);
    }

    @Override
    public int getCount() {
        int n = 0;
        for(AtomicBoolean flag : flags) {
            if(flag.get())
                n++;
        }
        return n;
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
    
    public boolean isEnabled(int position) {
        return flags.get(position).get();
    }

    public void setEnabled(int position, boolean enabled) {
        AtomicBoolean flag = flags.get(position);
        if(flag.get() != enabled) {
            flag.set(enabled);
            notifyDataSetChanged();
        }
    }
    
    public boolean isChatVisible() {
    	return ((getCount() == 4)? true : false);
    }
    public void addFragment(Fragment fragment, int position, Bundle args) {
		registeredFragments.append(position, fragment);
    	flags.add(new AtomicBoolean(true));
    }
    
    public void removeFragment(ViewPager pager, int position) {
    	pager.setAdapter(null);
    	registeredFragments.remove(position);
    	pager.setAdapter(this);
    }
}
