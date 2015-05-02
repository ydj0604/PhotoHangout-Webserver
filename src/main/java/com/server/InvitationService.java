package com.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/invitations")
public class InvitationService {
	@GET
	@Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInvitations(@PathParam("username") String username) {
		System.out.println(username);
		
		//TODO: get invitations to the user (of username)
		//TODO: return a list of invitation json objects
		
		
    	boolean verified = true;
    	if(verified)
    		return Response.status(200).build();
    	else
    		return Response.status(403).build();
    }
	
    @PUT
    @Path("/{invitationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateInvitation(Invitation invitation) {
    	System.out.println(invitation);
    	
    	//TODO: overwrite the invitation entry in Invitation Table
    	//TODO: if it doesn't exist, make a new one
    	
    	return Response.status(201).build();
    }
}
