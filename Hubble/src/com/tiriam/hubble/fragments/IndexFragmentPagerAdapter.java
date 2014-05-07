package com.tiriam.hubble.fragments;

import java.util.ArrayList;

import android.R;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IndexFragmentPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> frags = new ArrayList<Fragment>();
	Context context;
	
    public IndexFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public int getItemPosition (Object object)
    {
        int index = frags.indexOf (object);
        if (index == -1)
          return POSITION_NONE;
        else
          return index;
    }

    @Override
    public int getCount ()
    {
    	return frags.size();
    }

    @Override
    public Object instantiateItem (ViewGroup container, int position)
    {
    	Fragment f = frags.get(position);
    	//super.instantiateItem(container, position);
		return f;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object)
    {
        frags.remove(position);
    }
    
    public void addFragment (Fragment f, int position)
    {
    	frags.add(position, f);
    }
    
    public Fragment getFragment(int position) {
        return frags.get(position);
    }
    
    public boolean isChatVisible() {
    	return ((getCount() == 4)? true : false);
    }
    
    public void removeFragment(ViewPager pager, int position) {
    	pager.setAdapter(null);
    	frags.remove(position);
    	pager.setAdapter(this);
    }

	@Override
	public Fragment getItem(int position) {
		return frags.get(position);
	}
}
