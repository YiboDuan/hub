package com.tiriam.hubble.persist;

import org.json.JSONException;
import org.json.JSONObject;



public class UserAccountManager {
	public boolean createAccount(String username, String password, String email) {
		boolean result = false;
		CPanelConnector con = new CPanelConnector();
		result = con.addUser(username,password,email);
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
