package com.tiriam.hubble.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tiriam.hubble.R;
import com.tiriam.hubble.activities.HubIndex;
import com.tiriam.hubble.fragments.CreateHubFragment.OnCreateHubListener;
import com.tiriam.hubble.persist.HubManager;

public class FeedFragment extends ListFragment {
	LocationUpdater mCallback;
	OpenChat cCallback;
	FeedItemAdapter adapter;
	HubManager hm;
	
	private double latitude;
	private double longitude;
	
	static FeedFragment newInstance() {
		FeedFragment f = new FeedFragment();
		return f;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_feed, container, false);
    	return v;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Bundle args = new Bundle();
        int hub = -1;
		try {
			hub = adapter.getItem(position).getInt("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		args.putInt("hubID", hub);
        args.putString("username", ((HubIndex)getActivity()).getUsername());
        cCallback.onFeedItemClick(args);
    }
    
    public void populateFeed() {
    	Bundle args = mCallback.onLocationRefresh();
    	latitude = args.getDouble("latitude");
    	longitude = args.getDouble("longitude");
    	hm = new HubManager();
    	JSONObject[] fenceList = null;
		try {
			//JSONArray arr = hm.getHubs(43.4721914, -80.5384629);
			JSONArray arr = hm.getHubs(latitude, longitude);
			fenceList = new JSONObject[arr.length()];
			for(int i = 0; i < arr.length(); i++) {
				fenceList[i] = arr.getJSONObject(i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	adapter = new FeedItemAdapter(getActivity(), fenceList);
    	setListAdapter(adapter);
    	adapter.notifyDataSetChanged();
    }

    public interface LocationUpdater {
    	public Bundle onLocationRefresh();
    }
    
    public interface OpenChat {
    	public void onFeedItemClick(Bundle args);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (LocationUpdater) activity;
            cCallback = (OpenChat) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement interfaces for callback");
        }
    }
}
