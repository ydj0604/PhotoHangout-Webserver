package com.server;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/photos")
public class PhotoService extends ServiceWrapper {
	
	@GET
	@Path("/{user_id}/{photo_id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getPhoto(@PathParam("username") String user_id, @PathParam("photo_id") String photo_id) {
		String photo_path = null;
		
		String sqlQuery = String.format(
				"SELECT COUNT(*), location from Photo where id = "
				+ "(SELECT photo_id FROM PhotoHangout.UserToPhoto WHERE user_id = %s AND photo_id = %s);"
				,user_id, photo_id);
		ResultSet rs = null;
		
		try {
			rs = db.runSql(sqlQuery);
			if(rs.next() && rs.getString("COUNT(*)").equals("1")) {
				photo_path = rs.getString("location");
				System.out.printf("Verified user %s has access to photo %s at location: %s\n", user_id, photo_id, photo_path);
			} else {
				System.out.printf("Denied user %s has access to photo %s at location: %s\n", user_id, photo_id, photo_path);
				return Response.status(403).build();
			}
		} catch (SQLException e) {
			return Response.status(403).build();
		}
		
		//TODO: verify user session
		PhotoStream stream = new PhotoStream(photo_path);
	    
	    return Response.ok(stream, "image/png") //TODO: set content-type of your file
	            .header("content-disposition", "attachment; filename = "+ photo_id)
	            .build();
    }

	@GET
	@Path("/{user_id}/photo-ids")
	@Produces("application/json")
    public Response getPhotosIds(@PathParam("username") String username) {
		System.out.println(username);
		//TODO: verify user session
		//TODO: get photo ids of user by scanning photo user-to-photo table
		//TODO: return a json array of photo json objects
		return Response.status(200).build(); 		
	}
	
    @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPhoto(@PathParam("username") String username, InputStream istream) {    	
    	//TODO: verify user session
    	//TODO: update user-to-photo table, photo table
    	
    	//String photo_path = null; //get path to store the photo
    	//TODO: write file to photo_path
    	
    	//TODO: photoId is assigned and returned to client
    	return Response.status(200).build();
    }
}
