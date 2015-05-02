package com.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sessions")
public class SessionService {
	@GET
	@Path("/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveSession(@PathParam("sessionId") String sessionId) {
		System.out.println(sessionId);
		
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
    public Response createSession(Session session) { //make calls to other services
    	System.out.println(session);
    	
    	//TODO: store into DB
    	//TODO: session id is assigned here and returned to client
    	//TODO: populate Invitation Table with invitations of this session
    	
    	return Response.status(201).build();
    }
    
    @PUT
    @Path("/{sessionId}/complete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeSession(@PathParam("sessionId") String sessionId) { //make calls to other services
    	System.out.println(sessionId);
    	
    	//TODO: mark the session expired in Session Table
    	//TODO: mark all the corresponding invitations expired in Invitation Table
    	//TODO: update User-To-Photo table so that collaborators get to keep the photo
    	
    	return Response.status(201).build();
    }
}
