package com.server;

import java.io.Serializable;

public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7274641432240904912L;
	private String username;
	private String password;

	public Account() {}
	
	public Account(String u, String p) {
		username = u;
		password = p;
	}
	
	public void setUsername(String u) {
		username = u;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(String p) {
		password = p;
	}
	
	public String getPassword() {
		return password;
	}
	
	@Override
	public String toString() {
		return "Account [username=" + username + ", password=" + password + "]";
	}
}

