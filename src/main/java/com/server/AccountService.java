package com.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.server.Account;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/accounts")
public class AccountService extends ServiceWrapper {
	
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
		}
    	
    	return Response.status(201).build();
    }
}
