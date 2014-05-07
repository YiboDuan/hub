package com.tiriam.hubble.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tiriam.hubble.R;

public class MenuFragment extends ListFragment {
	public static MenuFragment newInstance() {
		MenuFragment f = new MenuFragment();
		return f;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_menu, container, false);
    	String[] mOptions = getResources().getStringArray(R.array.menu_items);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(  
    		     inflater.getContext(), android.R.layout.simple_list_item_1,  
    		     mOptions);
    	setListAdapter(adapter);
    	return v;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    	
	}
}
