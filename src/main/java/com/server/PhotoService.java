package com.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
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
import javax.ws.rs.core.StreamingOutput;

import org.codehaus.jettison.json.JSONObject;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.amazonaws.util.json.JSONArray;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.core.header.FormDataContentDisposition;

//import com.amazonaws.services.s3.AmazonS3;


@Path("/photos")
public class PhotoService extends ServiceWrapper {

	/**
	 * return the photo owned by username with photo_id
	 * @param username
	 * @param photoId
	 * @return
	 */
	@GET
	@Path("/{photo_id}")
	@Produces("image/jpeg")
    public Response getPhotoDirect(@PathParam("username") String username, @PathParam("photo_id") String photoId) {
		//TODO: verify user session
	
    	System.out.println("get photo direct for " + photoId);
    	
//    	//get user id from username
//		String sqlQueryUsr = String.format("SELECT * FROM User WHERE user_name='%s'", username);
//		ResultSet rs;
//		String userId = null;
//		try {
//			rs = db.runSql(sqlQueryUsr);
//			if(!rs.isBeforeFirst()) { //invalid username
//				throw new NotFoundException();
//			}
//			rs.next();
//			userId = rs.getString("id");
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//			throw new NotFoundException();
//		}
		
		//get photo hash from photoId
    	ResultSet rs;
		String photoHash = null;
		String sqlQueryPhoto = String.format("SELECT * FROM Photo WHERE id='%s'", photoId);
		try {
			rs = db.runSql(sqlQueryPhoto);
			if(!rs.isBeforeFirst()) { //invalid username
				throw new NotFoundException();
			}
			rs.next();
			photoHash = rs.getString("location");
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new NotFoundException();
		}
		
		//check the photo storage directory
		File dir = new File("Photos");
    	if(!dir.exists())
    		throw new NotFoundException();
    	
    	//temporarily only support jpeg img
    	final String requestedFilePath = dir.getAbsolutePath() + "/" + photoHash + ".jpeg";
		
		//provide an image
	    StreamingOutput stream = new StreamingOutput() {
	        @Override
	        public void write(OutputStream output) throws IOException {
	        	FileInputStream input = new FileInputStream(requestedFilePath);
	        	try {
	        		int bytes = 0;
	        		while ((bytes = input.read()) != -1)
	        			output.write(bytes);
	        	} catch (Exception e) {
	        	  	e.printStackTrace();
	          	}
	        	input.close();
	        }
	    };
	    return Response.ok(stream).type("image/jpeg").build();
	}
	
	
	/**
	 * Upload a photo under username
	 * @param istream
	 * @param contentDispositionHeader
	 * @param username
	 * @return
	 */
    @POST
    @Path("{username}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Photo uploadPhotoDirect(@FormDataParam("data") InputStream istream,
    								@FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
    								@PathParam("username") String username) {
    	
    	//TODO: verify user session
    	//System.out.println(contentDispositionHeader.getFileName());
    	System.out.println("upload photo direct for " + username);
    	{}
    	//get user id from username
		String sqlQueryUsr = String.format("SELECT * FROM User WHERE user_name='%s'", username);
		ResultSet rs;
		String userId = null;
		try {
			rs = db.runSql(sqlQueryUsr);
			if(!rs.isBeforeFirst()) { //invalid username
				throw new NotFoundException();
			}
			rs.next();
			userId = rs.getString("id");
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new NotFoundException();
		}
    	
		//generate hash for new photo and create photo POJO
    	CryptoGenerator crypto = CryptoGenerator.getInstance();
    	Photo newPhoto = new Photo();
    	String newPhotoHash = crypto.nextPhotoName();
    	newPhoto.setLocation(newPhotoHash);
    	String sqlQuery = String.format("INSERT INTO Photo(location) VALUES ('%s')",
    			newPhotoHash);
    	Long newPhotoId;
    	
    	try {
    		newPhotoId = db.executeSql(sqlQuery);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException();
		}
    	newPhoto.setPhotoId(newPhotoId.toString());
    	
    	//add user ownership
    	sqlQuery = String.format("INSERT INTO UserToPhoto(user_id, photo_id) VALUES ('%s', '%s')",userId, newPhoto.getPhotoId());
    	try {
        	db.executeSql(sqlQuery);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException();
		}    	
    	
    	//now upload the actual file
    	File dir = new File("Photos");
    	if(!dir.exists())
    		dir.mkdir(); //make a directory to store photos
    	
    	File ofile = null;
    	OutputStream ofstream = null;
    	//temporarily only support jpeg img
    	String newFilePath = dir.getAbsolutePath() + "/" + newPhotoHash + ".jpeg";
    	
    	try {
    		ofile = new File(newFilePath);
        	ofstream = new FileOutputStream(ofile);
    		int numBytes = 0;
    		byte[] bytes = new byte[1024];
    		while ((numBytes = istream.read(bytes)) != -1) { 
    			ofstream.write(bytes, 0, numBytes);
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	} finally {
    		try {
				istream.close();
				ofstream.flush();
				ofstream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	return newPhoto;
    }

	@GET
	@Path("/{username}/all")
	@Produces("application/json")
    public String getPhotos(@PathParam("username") String username) {
		System.out.println("get photo ids of " + username);
		//TODO: verify user session
		
		//get user id from username
		String userId = null;
		String sqlQueryUsr = String.format("SELECT * FROM User WHERE user_name='%s'", username);
		ResultSet rs;
		try {
			rs = db.runSql(sqlQueryUsr);
			if(!rs.isBeforeFirst()) { //invalid username
				throw new NotFoundException();
			}
			rs.next();
			userId = rs.getString("id");
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new NotFoundException();
		}
		
		String sqlQuery = String.format(
				"SELECT * from Photo WHERE id IN (SELECT photo_id FROM UserToPhoto WHERE user_id = '%s');"
				,userId);
		
		rs = null;
		String photo_id = null;
		String photo_path = null;
		ArrayList<Photo> photoArr = new ArrayList<>();

		try {
			rs = db.runSql(sqlQuery);
			while(rs.next()) {
				photo_id = rs.getString("id");
				photo_path = rs.getString("location");
				Photo temp = new Photo(photo_id, photo_path);
				photoArr.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException();
		}
		
		JSONArray resp = new JSONArray(photoArr);
		return resp.toString();
	}

	
//	@GET
//	@Path("/{user_id}/{photo_id}")
//	@Produces(MediaType.APPLICATION_JSON)
//    public Photo getPhoto(@PathParam("user_id") String user_id, @PathParam("photo_id") String photo_id) {
//		//TODO: verify user session
//		Photo resp = null;
//		String photo_path = null;
//
//		String sqlQuery = String.format(
//				"SELECT location from Photo WHERE id = (SELECT photo_id FROM UserToPhoto WHERE user_id = '%s' AND photo_id = '%s');"
//				,user_id, photo_id);
//		ResultSet rs = null;
//		try {	
//			rs = db.runSql(sqlQuery);
//			if(rs.next()) {
//				photo_path = rs.getString("location");
//				resp = new Photo(photo_id, photo_path);
//				System.out.printf("Verified user %s has access to photo %s at location: %s\n", user_id, photo_id, photo_path);
//			} else {
//				System.out.printf("Denied user %s has access to photo %s at location: %s\n", user_id, photo_id, photo_path);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return resp;
//    }
//	@GET
//	@Path("/{username}/{photo_id}/direct")
//	@Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public Response getPhotoDirect(@PathParam("username") String username, @PathParam("photo_id") String photo_id) {
//		//TODO: verify user session
//		
//		//String photo_path = null; //TODO: get path from photo table
//		System.out.println("get photo direct for " + username);
//		
//	    StreamingOutput stream = new StreamingOutput() {
//	        @Override
//	        public void write(OutputStream output) throws IOException {
//	        	FileInputStream input = new FileInputStream("lion.jpeg");
//	        	try {
//	        		// TODO: write file content to output with photo_path
//	        		int bytes = 0;
//	        		while ((bytes = input.read()) != -1)
//	        			output.write(bytes);
//	        	} catch (Exception e) {
//	        	  	e.printStackTrace();
//	          	}
//	        	input.close();
//	        }
//	    };
//		
//	    return Response.ok(stream, "image/png") //TODO: set content-type of your file
//	            .header("content-disposition", "attachment; filename = "+ photo_id)
//	            .build();
//    }
//	
//    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
//    public Response uploadPhotoDirect(@PathParam("username") String username, InputStream istream)
//    @PUT
//    @Path("/{user_id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Photo uploadPhoto(@PathParam("user_id") String user_id) {
//    	//TODO: verify user session
//    	//TODO: update user-to-photo table, photo table
//    	//IMPORTANT: write file to photo_path to S3 client side
//    	//DONE: photoId is assigned and returned to client
//    	
//    	CryptoGenerator crypto = new CryptoGenerator();
//    	Photo photo = new Photo();
//    	String photo_name = crypto.nextPhotoName();
//    	photo.setLocation(photo_name);
//    	String sqlQuery = String.format("INSERT INTO Photo(location) VALUES ('%s')",
//    			photo_name);
//    	Long id;
//    	try {
//        	id = db.executeSql(sqlQuery);
//		} catch (Exception e) {
//			e.printStackTrace();
//			// please do not save
//			return photo; 
//		}
//    	photo.setPhotoId(id.toString());
//    	
//    	// Add user ownership
//    	sqlQuery = String.format("INSERT INTO UserToPhoto(user_id, photo_id) VALUES ('%s', '%s')",user_id, photo.getPhotoId());
//    	
//    	try {
//        	id = db.executeSql(sqlQuery);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	    	
//    	return photo;        
//    }
}
