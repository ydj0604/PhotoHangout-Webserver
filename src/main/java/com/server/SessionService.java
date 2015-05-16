package com.server;

import java.sql.ResultSet;
import java.sql.SQLException;

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
			if(!rs.isBeforeFirst()) {
				return null;
			}
			rs.next();			
			resp = new Session(rs.getString("id"), rs.getString("owner_id"), rs.getString("photo_id"));
			resp.setExpireTime(rs.getString("expire_time"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resp;
    }

	@GET
	@Path("/{sessionId}/photohash")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPhotoHashFromSession(@PathParam("sessionId") String sessionId) {
		System.out.println("getSession: " + sessionId);
		//TODO:verify with DB
		
		ResultSet rs = null;
		String photoHash = null;
		
		try {
			rs = db.runSql("SELECT * FROM Session WHERE id=" + sessionId);
			if(!rs.isBeforeFirst()) {
				return null;
			}
			rs.next();
			String photoId = rs.getString("photo_id");
			rs = db.runSql("SELECT * FROM Photo WHERE id=" + photoId);
			photoHash = rs.getString("location");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return photoHash;
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
    @Path("/{sessionId}/joinstats}")
    @Produces(MediaType.APPLICATION_JSON)
    public String joinStats(@PathParam("sessionId") String sessionId) {
	   	String sqlQuery = String.format(
	   			"SELECT Count(*) as total FROM PhotoHangout.Invitation WHERE SESSION_id = %s",
	   			sessionId);
		ResultSet rs = null;
	
		JSONObject jo = new JSONObject();
	
		int joined;
		int total;
		
		try {
			rs = db.runSql(sqlQuery);
			total = Integer.parseInt(rs.getString("total"));
			jo.put("total", total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		rs = null;
		jo = new JSONObject();
		
		sqlQuery = String.format(
	   			"SELECT Count(*) as joined FROM PhotoHangout.Invitation WHERE SESSION_id = %s and accepted = 1" ,
	   			sessionId);
		try {
			rs = db.runSql(sqlQuery);
			joined = Integer.parseInt(rs.getString("joined"));
			jo.put("joined", joined);
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
