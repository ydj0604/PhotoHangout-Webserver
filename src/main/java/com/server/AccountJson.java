package com.server;

public class AccountJson {
	private String m_username;
	private String m_password;

	void setUsername(String u) {
		m_username = u;
	}
	
	String getUsername() {
		return m_username;
	}
	
	void setPassword(String p) {
		m_password = p;
	}
	
	String getPassword() {
		return m_password;
	}
	
	@Override
	public String toString() {
		return new StringBuffer(" Username : ").append(m_username)
				.append(" Password : ").append(m_password)
				.toString();
	}
}

