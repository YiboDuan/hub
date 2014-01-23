package com.tiriam.hubble;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Title extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.title, menu);
		return true;
	}
	
	/** Sends to login view 
	 * @throws JSONException **/
	public void login(View view) throws JSONException {
		CredentialVerifier ver = new CredentialVerifier();
		EditText v = (EditText) findViewById(R.id.l_username);
		String username = v.getText().toString();
		v = (EditText) findViewById(R.id.l_password);
		String password = v.getText().toString();
		if(ver.checkLogin(username, password)) {
			Intent intent = new Intent(this, HubIndex.class);
			startActivity(intent);
		} else {
			TextView t = (TextView) findViewById(R.id.login_fail);
			t.setVisibility(View.VISIBLE);
		}
	}
	
	/** Goes to create account view **/
	public void createAccount(View view) {
	    Intent intent = new Intent(this, AccountCreation.class);
	    startActivity(intent);
	}	
}
