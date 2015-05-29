package com.server;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountServiceTest {

    private HttpServer server;
    private WebTarget target;
	String testUserName = "JUnitTestUser";
	String testUserPass = "JUnitTestPass";
	Account testAccount = null;
    
    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        
        // create the client
        Client c = ClientBuilder.newClient();
        
        // set base url
        target = c.target(Main.BASE_URI);
        
        // create a temporary account for testing
        testAccount = new Account(testUserName, testUserPass);
        Response resp = target.path("accounts/").request().post(Entity.json(testAccount), Response.class);
        assertEquals(resp.getStatus(), 201);
    }

    @After
    public void tearDown() throws Exception {
    	// delete the temp account
    	Response resp = target.path("accounts/" + testUserName).request().delete(Response.class);
    	assertEquals(resp.getStatus(), 200);
    	
    	// stop the server
        server.shutdown();
    }

    @Test
    public void testLogin() {
    	Response respValid = target.path("accounts/login").request().post(Entity.json(testAccount), Response.class);
    	assertEquals(respValid.getStatus(), 200);
    	
    	Account invalidAccount = new Account("invalidusername", "invalidpw");
    	Response respInvalid = target.path("accounts/login").request().post(Entity.json(invalidAccount), Response.class);
    	assertEquals(respInvalid.getStatus(), 403);
    }
    
    @Test
    public void testGetAccount() {
    	Account respAcct = target.path("accounts/" + testUserName).request().get(Account.class);
    	assertEquals(respAcct.getUsername(), testAccount.getUsername());
    	assertEquals(respAcct.getPassword(), testAccount.getPassword());
    }    
}
