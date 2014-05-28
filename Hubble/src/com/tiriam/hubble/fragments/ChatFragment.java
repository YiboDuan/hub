package com.tiriam.hubble.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.tiriam.hubble.R;

public class ChatFragment extends Fragment {
	private Handler rhandler = new Handler();
	private Socket socket;
	private Thread rthread;
	private PrintWriter out;
	private BufferedReader in;
	
	private static final int SERVERPORT = 9999;
	private static final String SERVER_IP = "vps.futureaccess.ca";
	
	public ArrayAdapter<String> msgList;
	public ListView msgView;
	
	int hubID;
	
	public static ChatFragment newInstance(Bundle args) {
		ChatFragment f = new ChatFragment();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getArguments() != null) {
			connect();
		}

	}
	
	private void connect() {
		hubID = getArguments().getInt("id");
		listenSocket();
		receiveMessage();
		// send the server the username and hub name to be stored
	    this.out.println(getArguments().getString("username")
	    		+ "," + hubID);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.activity_chat, container, false);
		msgView = (ListView) v.findViewById(R.id.chat_display);
		msgList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		msgView.setAdapter(msgList);
    	return v;
    }

	public void listenSocket(){
	   try{
		 socket = new Socket(SERVER_IP, SERVERPORT);
		 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	     out = new PrintWriter(socket.getOutputStream(), true);
	   } catch (UnknownHostException e) {
	     System.out.println("Unknown host: futureaccess");
	     System.exit(1);
	   } catch  (IOException e) {
	     System.out.println("No I/O");
	     System.exit(1);
	   }
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	public void onClick(View view) {
		EditText et = (EditText) view.findViewById(R.id.chat_input);
		String str = et.getText().toString();
		out.println(hubID + ":" + str);
	}
	
	public void receiveMessage()
	{
		MessageReceiver mr = new MessageReceiver(in);
		rthread = new Thread(mr);
		rthread.start();
	}
	
	public void displayMessage(String msg)
	{ 
		final String mssg = msg;
	    rhandler.post(new Runnable() {
			@Override
			public void run() {
				msgList.add(mssg);
				msgView.setAdapter(msgList);
				msgView.smoothScrollToPosition(msgList.getCount() - 1);
			}
		});
	}
	
	public String getPageTitle() {
		if(getArguments() != null) {
			return getArguments().getString("hubName");
		}
		return "";
	}
	
	class MessageReceiver implements Runnable {
		private BufferedReader in;
		MessageReceiver(BufferedReader in) {
			this.in = in;
		}
		@Override
		public void run() {
			String msg;
	    	while(true)	{
	    		msg = null;
				try {
					msg = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
	    		if(msg == null)	{
	    			break;
	    		} else {
    				displayMessage(msg);
	    		}
	    	}
		}
	}
}
