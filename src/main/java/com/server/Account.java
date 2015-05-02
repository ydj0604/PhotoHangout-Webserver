package com.server;

import java.io.Serializable;

public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7274641432240904912L;
	private String Username;
	private String Password;
	private String Token; //to verify the current user

	public Account() {}
	
	public Account(String u, String p) {
		Username = u;
		Password = p;
	}
	
	public void setUsername(String u) {
		Username = u;
	}
	
	public String getUsername() {
		return Username;
	}
	
	public void setPassword(String p) {
		Password = p;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public void setToken(String t) {
		Token = t;
	}
	
	
	public String getToken() { 
		return Token;
	}
	
	@Override
	public String toString() {
		return "Account [username=" + Username + ", password=" + Password + ", token=" + Token + "]";
	}
}

