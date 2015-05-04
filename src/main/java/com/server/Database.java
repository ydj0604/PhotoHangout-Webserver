package com.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.mysql.jdbc.PreparedStatement;

public class Database {
	public static final String DB_URL = "jdbc:mysql://162.243.153.67:3306/PhotoHangout";
    public static final String DB_USER = "scriptor";
    public static final String DB_PASS = "obsecure";
    private static Database instance = new Database(DB_URL, DB_USER, DB_PASS);
	private Connection conn = null;	
	
    private Database(String url, String user_name, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection(url, user_name, password);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public static Database getInstance() {
    	return instance;
    }
    
    
    public Connection getConnection() {
    	return this.conn;
    }
    
    
    public ResultSet runSql(String sql) throws SQLException { //for select
    	Statement sta = conn.createStatement();
    	return sta.executeQuery(sql);
    }
    
    public Long executeSql(String sql) throws SQLException { //for insert
    	//Statement statement = conn.createStatement();
    	PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    	statement.executeUpdate();
    	ResultSet rs = statement.getGeneratedKeys();
    	if(rs.next())
    		return rs.getLong(1);
    	else
    		return null;
    }
    
    public Long executeSqlWithTimestamp(String sql) throws SQLException { //need to mark timestamp field with ?
    	PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    	statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
    	statement.executeUpdate();
    	ResultSet rs = statement.getGeneratedKeys();
    	if(rs.next())
    		return rs.getLong(1);
    	else
    		return null;
    }
}

//Connection connection = db.getConnection();
//Statement statement = null;
//ResultSet rs = null;
//try {
//	statement = connection.createStatement();
//	statement.executeUpdate("INSERT INTO Photo (location) VALUES('Manish')");
//} catch (SQLException e) {
//	System.out.println("SQLException Occured..");
//} finally {
//	try {
//		if (rs != null) {
//			rs.close(); // close result set
//		}
//
//		if (statement != null) {
//			statement.close(); // close statement
//		}
//
//		if (connection != null) {
//			connection.close(); // close connection
//		}
//	} catch (SQLException e) {
//		System.out.println("SQLException Occured..");
//	}
//}

