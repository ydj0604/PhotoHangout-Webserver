package com.server;

import java.io.Serializable;

public class Session implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1703610865463811284L;
	private String SessionId;
	private String OwnerId;
	private String PhotoId;
	private String ExpireTime;

	public Session() {}
	
	public Session(String s, String o, String p) {
		SessionId = s;
		OwnerId = o;
		PhotoId = p;
	}
	
	public void setSessionId(String s) {
		SessionId = s;
	}
	
	public String getSessionId() {
		return SessionId;
	}
	
	public void setOwnerId(String o) {
		OwnerId = o;
	}
	
	public String getOwnerId() {
		return OwnerId;
	}
	
	public void setPhotoId(String p) {
		PhotoId = p;
	}
	
	public String getPhotoId() {
		return PhotoId;
	}
	
	public void setExpireTime(String e) {
		ExpireTime = e;
	}
	
	public String getExpireTime() {
		return ExpireTime;
	}
}

