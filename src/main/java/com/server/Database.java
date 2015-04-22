package com.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	private Connection conn = null;
	
    public Database(String url, String user_name, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection(url, user_name, password);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public Connection getConnection() {
    	return this.conn;
    }
    
    
    public ResultSet runSql(String sql) throws SQLException {
    	Statement sta = conn.createStatement();
    	return sta.executeQuery(sql);
    }
}
