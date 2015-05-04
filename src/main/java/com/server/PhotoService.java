package com.server;

import java.sql.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;


@Path("/photos")
public class PhotoService extends ServiceWrapper {
	
	@GET
	@Path("/{user_id}/{photo_id}")
	@Produces(MediaType.APPLICATION_JSON)
    public Photo getPhoto(@PathParam("user_id") String user_id, @PathParam("photo_id") String photo_id) {
		//TODO: verify user session
		Photo resp = null;
		String photo_path = null;

		String sqlQuery = String.format(
				"SELECT location from Photo WHERE id = (SELECT photo_id FROM UserToPhoto WHERE user_id = '%s' AND photo_id = '%s');"
				,user_id, photo_id);
		ResultSet rs = null;
		try {
			rs = db.runSql(sqlQuery);
			if(rs.next()) {
				photo_path = rs.getString("location");
				resp = new Photo(photo_id, photo_path);
				System.out.printf("Verified user %s has access to photo %s at location: %s\n", user_id, photo_id, photo_path);
			} else {
				System.out.printf("Denied user %s has access to photo %s at location: %s\n", user_id, photo_id, photo_path);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resp;
    }

	@GET
	@Path("/{user_id}/photo-ids")
	@Produces("application/json")
    public String getPhotosIds(@PathParam("user_id") String user_id) {
		System.out.println(user_id);
		//TODO: verify user session
		//TODO: get photo ids of user by scanning photo user-to-photo table
		//TODO: return a json array of photo json objects
		
		String sqlQuery = String.format(
				"SELECT id, location from Photo WHERE id = (SELECT photo_id FROM UserToPhoto WHERE user_id = '%s');"
				,user_id);
		ResultSet rs = null;
		String photo_id = null;
		String photo_path = null;
		JSONObject jo = new JSONObject();

		try {
			rs = db.runSql(sqlQuery);
			while(rs.next()) {
				photo_id = rs.getString("id");
				photo_path = rs.getString("location");
				jo.put(photo_id, photo_path);
				System.out.printf("Verified user %s has access to photo %s at location: %s\n", user_id, photo_id, photo_path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
	
//	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C:/Users/JingyuLiu/Desktop/xkito/";
	
    @PUT
    @Path("/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Photo uploadPhoto(@PathParam("user_id") String user_id) {
    	//TODO: verify user session
    	//TODO: update user-to-photo table, photo table
    	//IMPORTANT: write file to photo_path to S3 client side
    	//DONE: photoId is assigned and returned to client
    	
    	CryptoGenerator crypto = new CryptoGenerator();
    	Photo photo = new Photo();
    	String photo_name = crypto.nextPhotoName();
    	photo.setLocation(photo_name);
    	String sqlQuery = String.format("INSERT INTO Photo(location) VALUES ('%s')",
    			photo_name);
    	try {
        	createPhoto(photo, sqlQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	// Add user ownership
    	sqlQuery = String.format("INSERT INTO UserToPhoto(user_id, photo_id) VALUES ('%s', '%s')", photo.getPhotoId(), user_id);
    	try {
    		Connection connection = db.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch(Exception e){
        	e.printStackTrace();
        }
    	return photo;        
    }
    
    
    public void createPhoto(Photo photo, String query) {
        try (
                Connection connection = db.getConnection();
                PreparedStatement statement = connection.prepareStatement(query,
                                              Statement.RETURN_GENERATED_KEYS);
            ) {

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        photo.setPhotoId(generatedKeys.getString(1));
                    }
                    else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }
        catch(Exception e){
        	e.printStackTrace();
        }
    }

}
