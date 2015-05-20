package com.server;

import java.io.Serializable;

public class Invitation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2518190102665499448L;
	private String InvitationId;
	private String SessionId;
	private String ReceiverId;
	private Integer Accepted;
	private String ExpireTime;
	private String photoId;
	private String hostId;

	public Invitation() {}

	public Invitation(String i, String s, String r) {
		InvitationId = i;
		SessionId = s;
		ReceiverId = r;
	}	
	
//	public Invitation(String i, String s, String r, Integer a, String e) {
//		InvitationId = i;
//		SessionId = s;
//		ReceiverId = r;
//		Accepted = a;
//		Expired = e;
//	}
	
	public void setInvitationId(String i) {
		InvitationId = i;
	}
	
	public String getInvitationId() {
		return InvitationId;
	}
	
	public void setSessionId(String s) {
		SessionId = s;
	}
	
	public String getSessionId() {
		return SessionId;
	}
	
	public void setReceiverId(String r) {
		ReceiverId = r;
	}
	
	public String getReceiverId() { 
		return ReceiverId;
	}
	
	public void setAccepted(Integer a) {
		Accepted = a;
	}
	
	public Integer getAccepted() {
		return Accepted;
	}
	
	public void setExpireTime(String e) {
		ExpireTime = e;
	}
	
	public String getExpireTime() {
		return ExpireTime;
	}
	
	public void setPhotoId(String p) {
		photoId = p;
	}
	
	public String getPhotoId() {
		return photoId;
	}
	
	public void setHostId(String h) {
		hostId = h;
	}
	
	public String getHostId() {
		return hostId;
	}
}

