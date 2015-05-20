package com.server;

import java.io.Serializable;

public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7274641432240904912L;
	private String Username;
	private String Password;
	private String Email;
	private String Token; //to verify the current user
	private String UserId;

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

	public String getEmail() {
		return Email;
	}
	
	public void setEmail(String e) {
		Email = e;
	}
	
	public String getToken() { 
		return Token;
	}
	
	public void setUserId(String i) {
		UserId = i;
	}
	
	public String getUserId() { 
		return UserId;
	}
	
	@Override
	public String toString() {
		return "Account [username=" + Username + ", password=" + Password + ", email=" + Email + "]";
	}
}

