package com.server;

import java.io.Serializable;

public class Session implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1703610865463811284L;
	private String SessionId;
	private String Owner;
	private String PhotoId;
	private Integer Expired; //1 means expired; 0 means not expired

	public Session() {}
	
	public Session(String s, String o, String p, Integer e) {
		SessionId = s;
		Owner = o;
		PhotoId = p;
		Expired = e;
	}
	
	public void setSessionId(String s) {
		SessionId = s;
	}
	
	public String getSessionId() {
		return SessionId;
	}
	
	public void setOwner(String o) {
		Owner = o;
	}
	
	public String getOwner() {
		return Owner;
	}
	
	public void setPhotoId(String p) {
		PhotoId = p;
	}
	
	public String getPhotoId() {
		return PhotoId;
	}
	
	public void setExpired(Integer e) {
		Expired = e;
	}
	
	public Integer getExpired() {
		return Expired;
	}
	
	@Override
	public String toString() {
		return "Session [sessionId=" + SessionId + ", owner=" + Owner + ", photoId=" + PhotoId + ", expired=" + Expired.toString() + "]";
	}
}

