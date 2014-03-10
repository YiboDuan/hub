package com.tiriam.hubble.persist;

import org.json.JSONArray;
import org.json.JSONException;

public class HubManager {
	CPanelConnector con;
	
	public HubManager() {
		con = new CPanelConnector();
	}
	
	public boolean createHub(String name, double latitude, double longitude, String creator, String privacy, int radius, int max, String password) {
		boolean result = false;
		result = con.createHub(name, latitude, longitude, creator, privacy, radius, max, password);
		return result;
	}
	
	public JSONArray getHubs(double latitude, double longitude) throws JSONException {
		JSONArray arr = con.getHubs(latitude, longitude);
		
		return arr;
	}
}