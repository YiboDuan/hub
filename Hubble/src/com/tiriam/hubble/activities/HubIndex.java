package com.tiriam.hubble.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.tiriam.hubble.R;
import com.tiriam.hubble.fragments.ChatFragment;
import com.tiriam.hubble.fragments.CreateHubFragment;
import com.tiriam.hubble.fragments.FeedFragment;
import com.tiriam.hubble.fragments.MenuFragment;
import com.tiriam.hubble.fragments.IndexFragmentPagerAdapter;

public class HubIndex extends ActionBarActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		CreateHubFragment.OnCreateHubListener,
		FeedFragment.LocationUpdater,
		FeedFragment.OpenChat {
	
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	IndexFragmentPagerAdapter mAdapter;
    ViewPager mPager;
    LocationClient mLocationClient;
    Location mLocation;
    String username;
    
    final int MENU_INDEX = 0;
    final int HUB_INDEX = 1;
    final int CREATEHUB_INDEX = 2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setTheme(R.style.IndexTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_index);
        setupActionBar();
        username = getIntent().getExtras().getString("username");
        if(servicesConnected()) {
        	mLocationClient = new LocationClient(this, this, this);
        }
        
        mAdapter = new IndexFragmentPagerAdapter(getSupportFragmentManager(), this);
        
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        Bundle args = new Bundle();
        mAdapter.addFragment(new MenuFragment(), 0, args);
        mAdapter.addFragment(new FeedFragment(), 1, args);
        mAdapter.addFragment(new CreateHubFragment(), 2, args);
        mAdapter.notifyDataSetChanged();
        mPager.setCurrentItem(1);
    }
    
    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
      // Connect the client.
        mLocationClient.connect();
        super.onStart();
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    private void setupActionBar() {
        ActionBar ab = getSupportActionBar();

        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.RIGHT);
        View v = inflator.inflate(R.layout.index_actionbar,null);
        
        ab.setCustomView(v,layout);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
    }
    
    // Menu Button
    public void toggleMenu(View view) {
    	mPager.setCurrentItem(MENU_INDEX, true);
    }
    
    // Create Button
    public void createAccount(View view) {
    	mPager.setCurrentItem(CREATEHUB_INDEX, true);
    }
    
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }    
    
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
             // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(),
                        "Location Updates");
            }
            return false;
        }
    }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                result.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
        	Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
        			result.getErrorCode(),
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
        	errorDialog.show();
        }
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		FeedFragment feed = (FeedFragment)mAdapter.getRegisteredFragment(HUB_INDEX);
		feed.populateFeed();
	}

	@Override
	public void onDisconnected() {
		 Toast.makeText(this, "Disconnected. Please re-connect.",
	                Toast.LENGTH_SHORT).show();
	}

	@Override
	public Bundle onHubSubmitted() {
		mLocation = mLocationClient.getLastLocation();
		double latitude = mLocation.getLatitude();
		double longitude = mLocation.getLongitude();
		Bundle args = new Bundle();
		args.putString("username", username);
		args.putDouble("latitude", latitude);
		args.putDouble("longitude", longitude);
		return args;
	}
	
	public String getUsername() {
		return username;
	}

	@Override
	public Bundle onLocationRefresh() {
		mLocation = mLocationClient.getLastLocation();
		double latitude = mLocation.getLatitude();
		double longitude = mLocation.getLongitude();
		Bundle args = new Bundle();
		args.putDouble("latitude", latitude);
		args.putDouble("longitude", longitude);
		return args;
	}

	@Override
	public void onFeedItemClick(Bundle args) {
		if(!mAdapter.isChatVisible()) {
			mAdapter.addFragment(new CreateHubFragment(), 3, null);
			mAdapter.removeFragment(mPager, 2);
			mAdapter.addFragment(ChatFragment.newInstance(args), 2, args);
		}
	}
	
}