package com.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.sql.*;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    static final String BASE_URI = "http://localhost:8080/myapp/";
    static final String DB_URL = "jdbc:mysql://162.243.153.67:3306/PhotoHangout";

    static final String DB_USER = "scriptor";
    static final String DB_PASS = "obsecure";
    
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.server package
        final ResourceConfig rc = new ResourceConfig().packages("com.server");
        rc.register(JacksonFeature.class);
//        Database db = new Database(DB_URL, DB_USER, DB_PASS);
//        Connection connection = db.getConnection();
//		Statement statement = null;
//		ResultSet rs = null;
//        try {
//			statement = connection.createStatement();
//			statement.executeUpdate("INSERT INTO Photo (location) VALUES('Manish')");
//		} catch (SQLException e) {
//			System.out.println("SQLException Occured..");
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close(); // close result set
//				}
//
//				if (statement != null) {
//					statement.close(); // close statement
//				}
//
//				if (connection != null) {
//					connection.close(); // close connection
//				}
//			} catch (SQLException e) {
//				System.out.println("SQLException Occured..");
//			}
//		}
        
	
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%s\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.shutdown();
    }
}

