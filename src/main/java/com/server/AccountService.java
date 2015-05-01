package com.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.server.Account;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/account")
public class AccountService {	
	@GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestLogin(Account account) {
		//TODO:verify with DB
    	boolean verified = true;
    	if(verified)
    		return Response.status(200).build();
    	else
    		return Response.status(403).build();
    }
	
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {
    	//TODO: store into DB
    	System.out.println(account.getUsername());
    	System.out.println(account.getPassword());
    	return Response.status(201).build();
    }
}
