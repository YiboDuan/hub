package com.tiriam.hubble;

import com.tiriam.hubble.CPanelConnector;


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
}
