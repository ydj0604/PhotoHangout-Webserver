package com.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;

public class PhotoStream implements StreamingOutput {

	private String photoPath;
	
	public PhotoStream(String filePath) {
		this.photoPath = filePath;
	}
	
	@Override
	public void write(OutputStream output) throws IOException,
			WebApplicationException {
		// TODO Auto-generated method stub
	          BufferedOutputStream bus = new BufferedOutputStream(output);
	          try {
				// TODO: write file content to output with photo_path
				File file = new File(photoPath);
		        FileInputStream fizip = new FileInputStream(file);

				byte[] buffer2 = IOUtils.toByteArray(fizip);
		        bus.write(buffer2);
		        } catch (Exception e) {
	             e.printStackTrace();
	          }
	}
	

}
