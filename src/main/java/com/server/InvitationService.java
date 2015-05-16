package com.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/invitations")
public class InvitationService extends ServiceWrapper {
//	@GET
//	@Path("/{username}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public JSONArray getInvitations(@PathParam("username") String username) {
//		System.out.println(username);
//		
//		//TODO: get invitations to the user (of username)
//		//TODO: return a list of invitation json objects
//		
//		ResultSet rs = null;
//		ArrayList<Invitation> resp = new ArrayList<>();
//		String sqlQuery = String.format("SELECT * FROM Invitation WHERE user_name='%s'", username);
//		try {
//			rs = db.runSql(sqlQuery);
//			while(rs.next()){
//			Invitation temp = new Invitation(rs.getString("invitationId"),rs.getString("sessionId"),rs.getString("Receiver"),Integer.parseInt(rs.getString("Accepted")),Integer.parseInt(rs.getString("Expired")));
//			resp.add(temp); 
//			
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//    	JSONArray jsonArray = JSONArray.fromObject(resp); 
//		return jsonArray; 
//    }
//	
//    @PUT
//    @Path("/{invitationId}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response updateInvitation(Invitation invitation) {
//    	System.out.println(invitation);
//    	
//    	//TODO: overwrite the invitation entry in Invitation Table
//    	//TODO: if it doesn't exist, make a new one
//    	
//    	//two usages: 
//    	//Usage 1: for invited user to accept invitations -->update
//    	//Usage 2: for user who start session --> wait others accept --> insert
//    	
//    	String sqlQuery_insert = "INSERT INTO Invitation ("; // InvitationId'%s', SessionId='%s', RecieverId='%s'  WHERE id=%s", sessionId);
//		String sqlQuery_set = "UPDATE Invitation SET "; 
//    	ArrayList<String> fieldArr = new ArrayList<String>();
//		ArrayList<String> valueArr = new ArrayList<String>();
//		
//		
//		if(invitation.getInvitationId()!=null) {
//			fieldArr.add("InvitationId");
//			valueArr.add(invitation.getInvitationId());
//		}
//		
//		if(invitation.getSessionId()!=null){
//			fieldArr.add("SessionId"); 
//			valueArr.add(invitation.getSessionId()); 
//		}
//		
//		if(invitation.getReceiverId()!=null){ 
//			fieldArr.add("ReceiverId"); 
//			valueArr.add(invitation.getReceiverId()); 
//		}
//		
//		if(invitation.getAccepted()!=null){ 
//			fieldArr.add("Accepted"); 
//			valueArr.add(String.valueOf((invitation.getAccepted()))); 
//		}
//			
//		if(invitation.getExpired()!=null){ 
//			fieldArr.add("Expired"); 
//			valueArr.add(String.valueOf((invitation.getExpired()))); 
//		}
//		
//		if(fieldArr.size()==0)
//			return Response.status(200).build();
//		 
//		String temp = "";
//		for(int i=0; i<fieldArr.size(); i++) {
//			if(i!=fieldArr.size()-1) {
//				String curr = String.format("%s ,", fieldArr.get(i));
//				temp += curr;
//			} else {
//				String curr = String.format("%s )", fieldArr.get(i));
//				temp += curr;
//			}
//		}
//		
//		sqlQuery_insert += temp + "VALUES ("; 
//		temp = "";
//		
//		for(int i=0; i<valueArr.size(); i++) {
//			if(i!=valueArr.size()-1) {
//				String curr = String.format("%s ,", valueArr.get(i));
//				temp += curr;
//			} else {
//				String curr = String.format("%s )", valueArr.get(i));
//				temp += curr;
//			}
//		}
//		
//		sqlQuery_insert += temp +"WHERE no exist(SELECT * from Invitation WHERE SessionId="+invitation.getSessionId() +" AND Reciever=" +invitation.getReceiverId(); 
//		System.out.println(sqlQuery_insert);
//		
//    	try {
//			db.executeSql(sqlQuery_insert);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return Response.status(403).build();
//		}
//    	
//    	
//    
//		sqlQuery_set += "Accpeted= " + String.valueOf((invitation.getAccepted()))+ ", Expired=" +String.valueOf((invitation.getExpired()));  
//		sqlQuery_set += " WHERE InvitationId= "+ invitation.getInvitationId(); 
//    	
//		try {
//			db.executeSql(sqlQuery_set);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return Response.status(403).build();
//		}
//    	
//		return Response.status(200).build();
//    	
//    }
}
