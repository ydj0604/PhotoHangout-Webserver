package com.server;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.server.AccountJson;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("account")
public class AccountResource {
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountJson account) {
    	System.out.println(account.getUsername());
    	System.out.println(account.getPassword());
    	return Response.status(201).build();
    }
}
