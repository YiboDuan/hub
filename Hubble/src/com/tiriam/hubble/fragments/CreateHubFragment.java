package com.tiriam.hubble.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.tiriam.hubble.R;
import com.tiriam.hubble.persist.HubManager;

public class CreateHubFragment extends Fragment implements 
		View.OnClickListener,
		CompoundButton.OnCheckedChangeListener {
	
	View v;
	OnCreateHubListener mCallback;
	
	static CreateHubFragment newInstance() {
		CreateHubFragment f = new CreateHubFragment();
		return f;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_create_hub, container, false);
        
        Switch s = (Switch)v.findViewById(R.id.set_visibility);
        s.setOnCheckedChangeListener(this);
        
        Spinner spinner = (Spinner)v.findViewById(R.id.set_radius);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.radius, 
        		android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        Button createButton = (Button)v.findViewById(R.id.create_hub_button);
        createButton.setOnClickListener(this);
        return v;
    }
    
    public interface OnCreateHubListener {
    	public Bundle onHubSubmitted();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCreateHubListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCreateHubListener");
        }
    }
    
	@Override
	public void onClick(View view) {
		Bundle args = mCallback.onHubSubmitted();
		// get username
		String username = args.getString("username");
		// get hubname
		EditText t = (EditText) v.findViewById(R.id.hubname);
		String hubname = t.getText().toString();
		// get privacy
		Switch s = (Switch) v.findViewById(R.id.set_visibility);
		String privacy;
		// default password
		String password = "-1";
		if(s.isChecked()) {
			privacy = "private";
			t = (EditText) v.findViewById(R.id.hub_password);
			password = t.getText().toString();
		} else {
			privacy = "public";
		}
		Spinner sp = (Spinner) v.findViewById(R.id.set_radius);
		String radius_choice = sp.getSelectedItem().toString();
		int radius = 0;
		if(radius_choice.contains("100m")) {
			radius = 100;
		} else if(radius_choice.contains("500m")) {
			radius = 500;
		} else if(radius_choice.contains("1km")) {
			radius = 1000;
		} else if(radius_choice.contains("5km")) {
			radius = 5000;
		}
		t = (EditText) v.findViewById(R.id.max_users);
		int max = Integer.parseInt(t.getText().toString());
		
		
		double latitude = args.getDouble("latitude");
		double longitude = args.getDouble("longitude");
		HubManager hm = new HubManager();
		hm.createHub(hubname, latitude, longitude, username, privacy, radius, max, password);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		EditText t = (EditText)v.findViewById(R.id.hub_password);
        if(isChecked) {
           	t.setVisibility(View.VISIBLE);
        } else {
           	t.setVisibility(View.GONE);
        }
	}
}
