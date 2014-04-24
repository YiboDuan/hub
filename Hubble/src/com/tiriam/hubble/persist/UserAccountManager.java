package com.tiriam.hubble.persist;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



public class UserAccountManager {
	public boolean createAccount(String username, String password, String email) {
		boolean result = false;
		CPanelConnector con = new CPanelConnector();
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("method", "createuser"));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("email", email));
		con.post(params);
		return result;
	}
	
	public boolean updateAccount(String username, String password, String email) {
		boolean result = false;
		return result;
	}
	
	public boolean checkLogin(String username, String password) throws JSONException {
		boolean result = false;
		
		CPanelConnector con = new CPanelConnector();
		JSONObject user = con.getUserJson(username);
		if(user != null && password.equals(user.get("password").toString())) {
			result = true;
		}
		return result;
	}
}
