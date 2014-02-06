package com.tiriam.hubble.activities;

import com.tiriam.hubble.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreateHub extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.LightTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_create_hub);
		// Show the Up button in the action bar.
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_hub, menu);
		return true;
	}
}
