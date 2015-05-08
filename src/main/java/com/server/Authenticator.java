package com.server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Authenticator {
	
	//used to authenticate every request by comparing the current token of the user and the token given in each request
	public static boolean authenticate(String username, String token) {
		Database db = Database.getInstance();
		String sqlQuery = String.format("SELECT token FROM User WHERE user_name='%s'", username);
		ResultSet rs = null;
		boolean verified = false;
		
		try {
			rs = db.runSql(sqlQuery);
			if(rs.next() && rs.getString("token").equals(token)) {
				verified = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return verified;
	}
}
