package com.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3ServiceWrapper {
	
	private static AmazonS3 s3 = null;
	private static final String bucketName = "photohangout";
	
	public static AmazonS3 getS3Instance() {
		if (s3 == null) {
			connectS3Instance();
		} 
		return s3;
	}
	private S3ServiceWrapper() {}
	
	private static void connectS3Instance()	{
		AWSCredentials credentials = null;
		try {
		    credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
		    throw new AmazonClientException(
		            "Cannot load the credentials from the credential profiles file. " +
		        "Please make sure that your credentials file is at the correct " +
		        "location (~/.aws/credentials), and is in valid format.",
		            e);
		}
	
		s3 = new AmazonS3Client(credentials);
	    Region usWest1 = Region.getRegion(Regions.US_WEST_1);
	    s3.setRegion(usWest1);
		String key = "Testing1";
		
		System.out.println("===========================================");
		System.out.println("Getting Started with Amazon S3");
		System.out.println("===========================================\n");
		System.out.println("Listing buckets");
        for (Bucket bucket : s3.listBuckets()) {
            System.out.println(" - " + bucket.getName());
        }
        System.out.println();
        
        putPhoto(null, "helloworld");
	}
	
	
	public static void putPhoto(File file, String key) {
		try {
			s3.putObject(new PutObjectRequest(bucketName, key, file));
		} catch (AmazonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AmazonClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 /**
     * Creates a temporary file with text data to demonstrate uploading a file
     * to Amazon S3
     *
     * @return A newly created temporary file with text data.
     *
     * @throws IOException
     */
    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("sharedphotos", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("01234567890112345678901234\n");
        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
        writer.write("01234567890112345678901234\n");
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.close();

        return file;
    }
    
    
    public static void main(String[] args) {
    	S3ServiceWrapper s3 = new S3ServiceWrapper();
    }
}
