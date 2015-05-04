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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.multipart.FormDataParam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
	
    @POST
    @Path("/{username}/{fileName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPhoto(@PathParam("fileName") final String fileName,
    		   @FormDataParam("content") final InputStream uploadedInputStream) {
    	
        String uploadedFileLocation = SERVER_UPLOAD_LOCATION_FOLDER + fileName;
        // save it
        try {
            writeToFile(uploadedInputStream, uploadedFileLocation);
        } catch(Exception e) {
    		return Response.status(403).build(); 		
        }
		return Response.status(200).build(); 		
        
    	//TODO: verify user session
    	//TODO: update user-to-photo table, photo table
    	
    	//String photo_path = null; //get path to store the photo
    	//TODO: write file to photo_path
    	
    	//TODO: photoId is assigned and returned to client
    }
    
    
 // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws Exception {
        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        int read = 0;
        byte[] bytes = new byte[1024];

        out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }

}
