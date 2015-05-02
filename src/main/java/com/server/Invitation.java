package com.server;

import java.io.Serializable;

public class Invitation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2518190102665499448L;
	private String InvitationId;
	private String SessionId;
	private String Receiver;
	private Integer Accepted;
	private Integer Expired;

	public Invitation() {}
	
	public Invitation(String i, String s, String r, Integer a, Integer e) {
		InvitationId = i;
		SessionId = s;
		Receiver = r;
		Accepted = a;
		Expired = e;
	}
	
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
	
	public void setReceiver(String r) {
		Receiver = r;
	}
	
	public String getReceiver() { 
		return Receiver;
	}
	
	public void setAccepted(Integer a) {
		Accepted = a;
	}
	
	public Integer getAccepted() {
		return Accepted;
	}
	
	public void setExpired(Integer e) {
		Expired = e;
	}
	
	public Integer getExpired() {
		return Expired;
	}
	
	@Override
	public String toString() {
		return "Invitation [invitationId=" + InvitationId + ", sessionId=" + SessionId
				+ ", receiver=" + Receiver + ", accepted=" + Accepted.toString() + ", expired=" + Expired.toString() + "]";
	}
}

