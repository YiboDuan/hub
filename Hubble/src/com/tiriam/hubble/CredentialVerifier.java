package com.tiriam.hubble;

import org.json.JSONException;
import org.json.JSONObject;

public class CredentialVerifier {
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
