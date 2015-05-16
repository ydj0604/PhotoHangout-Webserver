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

import com.amazonaws.util.json.JSONArray;


@Path("/invitations")
public class InvitationService extends ServiceWrapper {
	@GET
	@Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getInvitations(@PathParam("username") String username) {
		System.out.println(username);
		
		//TODO: get invitations to the user (of username)
		//TODO: return a list of invitation json objects
		
		ResultSet rs = null;
		ArrayList<Invitation> resp = new ArrayList<>();
		
		String sqlQuery = String.format("SELECT * FROM Invitation WHERE receiver_id='%s'", username);
		try {
			rs = db.runSql(sqlQuery);
			if(!rs.isBeforeFirst()) {
				return "{}";
			}
			while(rs.next()){
				Invitation temp = new Invitation(rs.getString("id"),rs.getString("session_id"),rs.getString("receiver_id"));
				resp.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	JSONArray jsonArray = new JSONArray(resp);
		return "{" + jsonArray.toString() + "}";
    }
	
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Invitation createInvitation(Invitation invitation) {
    	System.out.println("create invitation: " + invitation);
    	    	
    	String sess_id = invitation.getSessionId();
    	String rcvr_id = invitation.getReceiverId();
    	Integer accepted = new Integer(0);
    	
    	String sqlQuery = String.format("INSERT INTO Invitation(session_id, receiver_id, accepted) VALUES ('%s', '%s', '%s')", sess_id, rcvr_id, accepted.toString());
    	Long generatedInvId= null;
    	
    	try {
			generatedInvId = db.executeSql(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	Invitation resp = null;
    	resp = new Invitation(generatedInvId.toString(), sess_id, rcvr_id);
    	return resp;
    }
    
    @PUT
    @Path("/{invitationId}/expire")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response expireInvitation(@PathParam("invitationId") String invitationId) {
    	System.out.println("expire invitation: " + invitationId);
    	
    	String sqlQuery = String.format("UPDATE Invitation SET expired_time= ? WHERE id=%s", invitationId);
    	
    	try {
			db.executeSqlWithTimestamp(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(404).build();
		}
    	
    	return Response.status(200).build();
    }

    @PUT
    @Path("/{invitationId}/accept")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptInvitation(@PathParam("invitationId") String invitationId) {
    	System.out.println("accept invitation: " + invitationId);
    	Integer accept = new Integer(1);
    	String sqlQuery = String.format("UPDATE Invitation SET accepted=%s WHERE id=%s", accept.toString(), invitationId);
    	
    	try {
    		db.executeSql(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(404).build();
		}
    	
    	return Response.status(200).build();
    }
    
}
