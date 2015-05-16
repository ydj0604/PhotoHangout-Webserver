package com.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.codehaus.jettison.json.JSONObject;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.sun.jersey.core.header.FormDataContentDisposition;

//import com.amazonaws.services.s3.AmazonS3;


@Path("/photos")
public class PhotoService extends ServiceWrapper {
	
//	private AmazonS3 s3 = S3ServiceWrapper.getS3Instance();

	@GET
	@Path("/{username}/{photo_id}/direct")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getPhotoDirect(@PathParam("username") String username, @PathParam("photo_id") String photo_id) {
		//TODO: verify user session
		
		//String photo_path = null; //TODO: get path from photo table

	    StreamingOutput stream = new StreamingOutput() {
	        @Override
	        public void write(OutputStream output) throws IOException {
	        	FileInputStream input = new FileInputStream("lion.jpeg");
	        	try {
	        		// TODO: write file content to output with photo_path
	        		int bytes = 0;
	        		while ((bytes = input.read()) != -1)
	        			output.write(bytes);
	        	} catch (Exception e) {
	        	  	e.printStackTrace();
	          	}
	        	input.close();
	        }
	    };
		
	    return Response.ok(stream, "image/png") //TODO: set content-type of your file
	            .header("content-disposition", "attachment; filename = "+ photo_id)
	            .build();
    }
	
    @POST
    @Path("/{username}/direct")
    //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //public Response uploadPhotoDirect(@PathParam("username") String username, InputStream istream) {
    public Response uploadPhotoDirect(@FormDataParam("data") InputStream istream,
    								@FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
    								@PathParam("username") String username) {
    	//TODO: verify user session
    	//TODO: update user-to-photo table, photo table
    	
    	//String photo_path = null; //get path to store the photo
    	//TODO: write file to photo_path
    	
    	//TODO: photoId is assigned and returned to client
    	File ofile = null;
    	OutputStream ofstream = null;
    	System.out.println(username);
    	
    	try {
    		ofile = new File("temp.jpeg");
        	ofstream = new FileOutputStream(ofile);
    		int numBytes = 0;
    		byte[] bytes = new byte[1024];
    		while ((numBytes = istream.read(bytes)) != -1) { 
    			ofstream.write(bytes, 0, numBytes);
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
				istream.close();
				ofstream.flush();
				ofstream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	return Response.status(200).build();
    }
	
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
				"SELECT * from Photo WHERE id IN (SELECT photo_id FROM UserToPhoto WHERE user_id = '%s');"
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
    	Long id;
    	try {
        	id = db.executeSql(sqlQuery);
		} catch (Exception e) {
			e.printStackTrace();
			// please do not save
			return photo; 
		}
    	photo.setPhotoId(id.toString());
    	
    	// Add user ownership
    	sqlQuery = String.format("INSERT INTO UserToPhoto(user_id, photo_id) VALUES ('%s', '%s')",user_id, photo.getPhotoId());
    	
    	try {
        	id = db.executeSql(sqlQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	    	
    	return photo;        
    }
}
