package com.tiriam.hubble;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

public class Chat extends ActionBarActivity {

	private Socket socket;

	private static final int SERVERPORT = 10011;
	private static final String SERVER_IP = "vps.futureaccess.ca";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.IndexTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		try {
			socket = new Socket(SERVER_IP, SERVERPORT);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	public void onClick(View view) {
		try {
			EditText et = (EditText) findViewById(R.id.chat_input);
			String str = et.getText().toString();
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.write(str);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
