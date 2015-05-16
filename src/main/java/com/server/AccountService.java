package com.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.server.Account;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/accounts")
public class AccountService extends ServiceWrapper {
	
	@GET
	@Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("username") String username) {
		ResultSet rs = null;
		Account resp = null;
		String sqlQuery = String.format("SELECT * FROM User WHERE user_name='%s'", username);
		
		try {
			rs = db.runSql(sqlQuery);
			if(!rs.isBeforeFirst()) {
				return null;
			}
			rs.next();
			resp = new Account(username, rs.getString("password"));
			resp.setUserId(rs.getString("id"));
			if(rs.getString("email") != null)
				resp.setEmail(rs.getString("email"));
			if(rs.getString("token") != null)
				resp.setToken(rs.getString("token"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return resp;
    }
	
	@PUT
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAccount(@PathParam("username") String username, Account account) {
		String sqlQuery = "UPDATE User SET "; // user_name='%s', password='%s', email= WHERE id=%s", sessionId);
		ArrayList<String> fieldArr = new ArrayList<String>();
		ArrayList<String> valueArr = new ArrayList<String>();
		
		if(account.getPassword()!=null) {
			fieldArr.add("password");
			valueArr.add(account.getPassword());
		}
		if(account.getEmail()!=null) {
			fieldArr.add("email");
			valueArr.add(account.getEmail());
		}
		if(account.getToken()!=null) {
			fieldArr.add("token");
			valueArr.add(account.getToken());
		}
		
		if(fieldArr.size()==0)
			return Response.status(200).build();
		
		String temp = "";
		for(int i=0; i<fieldArr.size(); i++) {
			if(i==0) {
				String curr = String.format("%s='%s'", fieldArr.get(i), valueArr.get(i));
				temp += curr;
			} else {
				String curr = String.format(", %s='%s'", fieldArr.get(i), valueArr.get(i));
				temp += curr;
			}
		}
		sqlQuery += temp + " WHERE user_name='" + username + "'";
		
		System.out.println(sqlQuery);
		
    	try {
			db.executeSql(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(403).build();
		}
    	return Response.status(200).build();
	}
	
	
	@POST
	@Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestLogin(Account account) {
		String sqlQuery = String.format("SELECT password FROM User WHERE user_name='%s'", account.getUsername());
		ResultSet rs = null;
		boolean verified = false;
		
		try {
			rs = db.runSql(sqlQuery);
			if(rs.next() && rs.getString("password").equals(account.getPassword())) {
				verified = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(403).build();
		}
    	
    	if(verified)
    		return Response.status(200).build();
    	else
    		return Response.status(403).build();
    }
	
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {    
    	String sqlQuery = String.format("INSERT INTO User(user_name, password, email) VALUES ('%s', '%s', '%s')",
    			account.getUsername(), account.getPassword(), account.getEmail()==null? "": account.getEmail());
    	   	
    	try {
			db.executeSql(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(403).build();
		}
    	
    	return Response.status(201).build();
    }
    
    
    public boolean accessAllowed(String deviceId) {
    	return true;
    }
}
