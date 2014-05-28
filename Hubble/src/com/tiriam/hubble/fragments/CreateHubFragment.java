package com.tiriam.hubble.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tiriam.hubble.R;
import com.tiriam.hubble.persist.HubManager;

public class CreateHubFragment extends Fragment implements View.OnClickListener{
	
	View v;
	OnCreateHubListener mCallback;
	
	//keep track of radio buttons to change them in click listeners
	RadioButton selectPublic;
	RadioButton selectPrivate;
	RadioButton smallSize;
	RadioButton mediumSize;
	RadioButton largeSize;
	RadioButton xlargeSize;
	ArrayList<RadioButton> sizeButtons = new ArrayList<RadioButton>();
	EditText passwordInput;
	
	boolean isPrivate = false;
	
	public static CreateHubFragment newInstance() {
		CreateHubFragment f = new CreateHubFragment();
		return f;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_create_hub, container, false);
        trackViews(v);
        setupRadioButtons(v);
        
        return v;
    }
    
    private void trackViews(View v) {
    	selectPublic = (RadioButton)v.findViewById(R.id.radio_public);
    	selectPrivate = (RadioButton)v.findViewById(R.id.radio_private);
    	passwordInput = (EditText)v.findViewById(R.id.hub_password);
    	sizeButtons.add((RadioButton)v.findViewById(R.id.small_size));
    	sizeButtons.add((RadioButton)v.findViewById(R.id.medium_size));
    	sizeButtons.add((RadioButton)v.findViewById(R.id.large_size));
    	sizeButtons.add((RadioButton)v.findViewById(R.id.xlarge_size));
    }
    
    private void setupRadioButtons(View v) {
    	RadioButton rb = (RadioButton)v.findViewById(R.id.radio_public);
        rb.setChecked(true);
        rb.setOnClickListener(onPrivacyCheckedListener);
        rb = (RadioButton)v.findViewById(R.id.radio_private);
        rb.setOnClickListener(onPrivacyCheckedListener);
        
        rb = (RadioButton)v.findViewById(R.id.radio_ten);
        rb.setChecked(true);

        rb = (RadioButton)v.findViewById(R.id.medium_size);
        rb.setChecked(true);
        
        for(RadioButton bt : sizeButtons) {
        	bt.setOnClickListener(onRadiusCheckedListener);
        }
        
        Button createButton = (Button)v.findViewById(R.id.create_hub_button);
        createButton.setOnClickListener(this);
    }
    
    private OnClickListener onPrivacyCheckedListener = new OnClickListener(){
    	@Override
    	public void onClick(View view) {
	        // Is the button now checked?
	        boolean checked = ((RadioButton)view).isChecked();
	        
	        // Check which radio button was clicked
	        switch(view.getId()) {
	            case R.id.radio_public:
	                if (checked) {
	                    isPrivate = false;
	                	selectPrivate.setChecked(false);
	                	passwordInput.setVisibility(View.GONE);
	                }
	                break;
	            case R.id.radio_private:
	                if (checked) {
	                	isPrivate = true;
	            		selectPublic.setChecked(false);;
	            		passwordInput.setVisibility(View.VISIBLE);
	                }
	                break;
	        }
    	}
    };
    
    private OnClickListener onRadiusCheckedListener = new OnClickListener(){
    	@Override
    	public void onClick(View view) {
	        // Is the button now checked?
	        boolean checked = ((RadioButton)view).isChecked();
	        
	        if (checked) {
	        	unselectOtherSizes((RadioButton) view);
	        }
    	}
    };
    
    public void unselectOtherSizes(View current) {
    	for(RadioButton bt : sizeButtons) {
    		if(bt.getId() != current.getId()) {
    			bt.setChecked(false);
    		}
    	}
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
		String privacy;
		// default password
		String password = "-1";
		if(isPrivate) {
			privacy = "private";
			t = (EditText) v.findViewById(R.id.hub_password);
			password = t.getText().toString();
		} else {
			privacy = "public";
		}
		
		RadioGroup maxUserGroup = (RadioGroup) v.findViewById(R.id.max_user_radio);
		int selectedMax = maxUserGroup.getCheckedRadioButtonId();
		
		int max_user = 10;
		if(selectedMax == R.id.radio_five) {
			max_user = 5;
		} else if (selectedMax == R.id.radio_ten) {
			max_user = 10;
		} else if (selectedMax == R.id.radio_fifteen) {
			max_user = 15;
		}
		
		int selectedRadius = 0;
		
    	for(RadioButton bt : sizeButtons) {
    		if(bt.isChecked()) {
    			selectedRadius = bt.getId();
    		}
    	}
		
		int radius = 0;
		if(selectedRadius == R.id.small_size) {
			radius = 100;
		} else if(selectedRadius == R.id.medium_size) {
			radius = 500;
		} else if(selectedRadius == R.id.large_size) {
			radius = 1000;
		} else if(selectedRadius == R.id.xlarge_size) {
			radius = 5000;
		}
		
		double latitude = args.getDouble("latitude");
		double longitude = args.getDouble("longitude");
		
		// send info to server for hub creation
		HubManager hm = new HubManager();
		boolean result = hm.createHub(hubname, latitude, longitude, username, privacy, radius, max_user, password);
		
		// notify user of result of creation attempt
		CharSequence text;
		if(result) {
			text = "Hub Created!";
		} else {
			text = "Unable to create hub.";
		}
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(getActivity(), text, duration);
		toast.show();
	}

}
