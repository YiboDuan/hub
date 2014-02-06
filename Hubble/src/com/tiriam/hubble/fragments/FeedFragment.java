package com.tiriam.hubble.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiriam.hubble.R;

public class FeedFragment extends Fragment{
	static FeedFragment newInstance() {
		FeedFragment f = new FeedFragment();
		return f;
	}
	
	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.temp_feed, container, false);
    	//GridView v = (GridView)inflater.inflate(R.layout.fragment_feed, container, false);
    	//v.setAdapter(new FeedItemAdapter());
    	/*
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(HelloGridView.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	    */
    	return v;
    }
}
