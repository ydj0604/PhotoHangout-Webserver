package com.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.json.JSONArray;

@Path("/sessions")
public class SessionService extends ServiceWrapper {
	
	@GET
	@Path("/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Session getSession(@PathParam("sessionId") String sessionId) {
		System.out.println("getSession: " + sessionId);
		//TODO:verify with DB
		
		ResultSet rs = null;		
		Session resp = null;
		
		try {
			rs = db.runSql("SELECT * FROM Session WHERE id=" + sessionId);
			rs.next();
			resp = new Session(rs.getString("id"), rs.getString("owner_id"), rs.getString("photo_id"));
			resp.setExpireTime(rs.getString("expire_time"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resp;
    }
	
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Session createSession(Session session) {
    	System.out.println("create session: " + session);
    	
    	Session resp = null;
    	//TODO: session id is assigned here and returned to client
    	String owner_id = session.getOwnerId();
    	String photo_id = session.getPhotoId();
    	String sqlQuery = String.format("INSERT INTO Session(owner_id, photo_id) VALUES ('%s', '%s')", owner_id, photo_id);
    	Long generatedSessionId= null;
    	
    	try {
			generatedSessionId = db.executeSql(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	resp = new Session(generatedSessionId.toString(), owner_id, photo_id);
    	
    	return resp;
    }

    @PUT
    @Path("/{sessionId}/expire")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response expireSession(@PathParam("sessionId") String sessionId) {
    	System.out.println("expire session: " + sessionId);
    	
    	String sqlQuery = String.format("UPDATE Session SET expire_time= ? WHERE id=%s", sessionId);
    	
    	try {
			db.executeSqlWithTimestamp(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(404).build();
		}
    	
    	return Response.status(200).build();
    }
    
    
    @GET
    @Path("/{sessionId}/joinstats")
    @Produces(MediaType.APPLICATION_JSON)
    public String joinAccepted(@PathParam("sessionId") String sessionId) {
	   	String sqlQuery = String.format(
	   			"SELECT Distinct PhotoHangout.User.id, user_name, accepted from PhotoHangout.User inner join PhotoHangout.Invitation on PhotoHangout.User.id = receiver_id where SESSION_id = %s and accepted = 1;",
	   			sessionId);
		ResultSet rs = null;
	
		String id = null;
		String user_name = null;
		String accepted = null;
		JSONArray jo = new JSONArray();
		try {
			rs = db.runSql(sqlQuery);
			while(rs.next()) {
				id = rs.getString("id");
				user_name = rs.getString("user_name");
				accepted = rs.getString("accepted");
				HashMap<String, String> col = new HashMap<String, String> ();
				col.put("id", id);
				col.put("user_name",user_name);
				col.put("accepted",accepted);
				jo.put(col);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
    
    
    /*
    @PUT
    @Path("/complete/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeSession(@PathParam("sessionId") String sessionId) { //make calls to other services
    	System.out.println(sessionId);
    	
    	//TODO: mark the session expired in Session Table
    	//TODO: mark all the corresponding invitations expired in Invitation Table
    	//TODO: update User-To-Photo table so that collaborators get to keep the photo
    	
    	return Response.status(201).build();
    }
    */
}
