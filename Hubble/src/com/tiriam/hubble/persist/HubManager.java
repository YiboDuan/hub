package com.tiriam.hubble.persist;

public class HubManager {
	public boolean createHub(String name, double latitude, double longitude, String creator, String privacy, int radius, int max, String password) {
		boolean result = false;
		CPanelConnector con = new CPanelConnector();
		result = con.createHub(name, latitude, longitude, creator, privacy, radius, max, password);
		return result;
	}
}