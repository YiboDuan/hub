package com.tiriam.hubble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Title extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.title, menu);
		return true;
	}
	
	/** Sends to login view **/
	public void login(View view) {

	}
	
	/** Goes to create account view **/
	public void createAccount(View view) {
	    Intent intent = new Intent(this, AccountCreation.class);
	    startActivity(intent);
	}

}
