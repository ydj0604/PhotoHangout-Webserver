package com.server;

public class ServiceWrapper {
	Database db;
	public ServiceWrapper() {
		db = Database.getInstance();
	}
}
