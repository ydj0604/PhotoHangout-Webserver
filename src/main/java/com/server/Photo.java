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
	private String photoId;
	private String path;

	public Photo() {}
	
	public Photo(String id, String p) {
		photoId = id;
		path = p;
	}
	
	public void setPhotoId(String id) {
		photoId = id;
	}
	
	public String getPhotoId() {
		return photoId;
	}
	
	public void setPath(String p) {
		path = p;
	}
	
	public String getPassword() {
		return path;
	}
	
	@Override
	public String toString() {
		return "Account [photoId=" + photoId + ", path=" + path + "]";
	}
}

