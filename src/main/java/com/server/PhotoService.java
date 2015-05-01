package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@Path("/photo")
public class PhotoService {
	
	@GET
	@Path("/{username}/{photo_id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getPhoto(@PathParam("username") String username, @PathParam("photo_id") String photo_id) {
		//TODO: verify user session
		
		String photo_path = null; //TODO: get path from photo table

	    StreamingOutput stream = new StreamingOutput() {
	        @Override
	        public void write(OutputStream output) throws IOException {
	          try {
	            // TODO: write file content to output with photo_path
	          } catch (Exception e) {
	             e.printStackTrace();
	          }
	        }
	      };
		
	    return Response.ok(stream, "image/png") //TODO: set content-type of your file
	            .header("content-disposition", "attachment; filename = "+ photo_id)
	            .build();
	    
	
	    /* 
	    	alternative 
		    response.setContentType("image/png");
		    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		    TODO: write file content to response.getOutputStream();
		    response.getOutputStream().close();
		    return response; 
	     */
    }

	@GET
	@Path("/{username}/photo-ids")
	@Produces("application/json")
    public Response getPhotosIds(@PathParam("username") String username) {
		//TODO: verify user session
		//TODO: get photo ids of user by scanning photo user-to-photo table
		//TODO: return a json array of photo json objects
		return Response.status(200).build(); 		
	}
	
    @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPhoto(@PathParam("username") String username, InputStream stream) {
    	//TODO: verify user session
    	//TODO: update user-to-photo table, photo table
    	
    	String photo_path = null; //get path to store the photo
    	//TODO: write file to photo_path
    	
    	return Response.status(200).build();
    }
}
