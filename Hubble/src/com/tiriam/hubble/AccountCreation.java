package com.tiriam.hubble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountCreation extends Activity {

	final Context context = this;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_account_creation);
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_creation, menu);
		return true;
	}
	
	public void createAccount(View view) {
		boolean result = false;
		EditText v = (EditText) findViewById(R.id.c_email);
		String email = v.getText().toString();
		v = (EditText) findViewById(R.id.c_username);
		String username = v.getText().toString();
		v = (EditText) findViewById(R.id.c_password);
		String password = v.getText().toString();
		v = (EditText) findViewById(R.id.c_confirmpassword);
		String confirm = v.getText().toString();
		if(password.equals(confirm)) {
			UserAccountManager uManager = new UserAccountManager();
			result = uManager.createAccount(username, password, email);
			if(result) {
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_success_creation);
	 
				TextView text = (TextView) dialog.findViewById(R.id.signup_notif);
				text.setText(R.string.success_login_dialog);
				Button dialogButton = (Button) dialog.findViewById(R.id.signup_ok);
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						launchIntent();
					}
				});
	 
				dialog.show();
			}

		} else {
			//TO-DO: notifications for info restrictions
		}
	}
	
	private void launchIntent() {
	    Intent it = new Intent(AccountCreation.this, Title.class);
	    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	    startActivity(it); 
	}

}
