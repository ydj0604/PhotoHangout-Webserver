package com.server;

import java.io.Serializable;

public class Photo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 94085000869976217L;
	/**
	 * 
	 */
	private String PhotoId;
	private String Location; //hash

	public Photo() {}
	
	public Photo(String id, String l) {
		PhotoId = id;
		Location = l;
	}
	
	public void setPhotoId(String id) {
		PhotoId = id;
	}
	
	public String getPhotoId() {
		return PhotoId;
	}
	
	public void setLocation(String l) {
		Location = l;
	}
	
	public String getLocation() {
		return Location;
	}
	
	@Override
	public String toString() {
		return "Account [photoId=" + PhotoId + ", location=" + Location + "]";
	}
}

