package com.tiriam.hubble.persist;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

public class HubManager {
	CPanelConnector con;
	
	public HubManager() {
		con = new CPanelConnector();
	}
	
	public boolean createHub(String name, double latitude, double longitude, String creator, String privacy, int radius, int max, String password) {
		boolean result = false;
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("method", "createhub"));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("latitude", Double.toString(latitude)));
		params.add(new BasicNameValuePair("longitude", Double.toString(longitude)));
		params.add(new BasicNameValuePair("creator", creator));
		params.add(new BasicNameValuePair("privacy", privacy));
		params.add(new BasicNameValuePair("radius", Float.toString(radius)));
		params.add(new BasicNameValuePair("max", Integer.toString(max)));
		params.add(new BasicNameValuePair("password", password));
		result = con.post(params);
		return result;
	}
	
	public JSONArray getHubs(double latitude, double longitude) throws JSONException {
		JSONArray arr = con.getHubs(latitude, longitude);
		
		return arr;
	}
}