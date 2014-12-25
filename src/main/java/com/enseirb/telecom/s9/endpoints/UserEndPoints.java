package com.enseirb.telecom.s9.endpoints;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.UserRepositoryMongo;
import com.enseirb.telecom.s9.service.AccountService;
import com.enseirb.telecom.s9.service.AccountServiceImpl;

// The Java class will be hosted at the URI path "/app/account"
@Path("app/account")
public class UserEndPoints extends HttpServlet {

	AccountService uManager = new AccountServiceImpl(new UserRepositoryMongo());


    	//@RolesAllowed("user")
		@GET
		@Path("/get")
		public Response addUser(@Context HttpHeaders headers) {//@HeaderParam("cookie") String userAgent) {
			String userAgent = headers.getRequestHeader("cookie").get(0);
			System.out.println(userAgent);
			return Response.status(200)
				.entity("addUser is called, userAgent : " + userAgent)
				.build();
	 
		}
	

	
	
	@POST()
	@Path("Connect")
	@Produces({ "application/json"})// resultat en JSON
	public Response getConnect(@HeaderParam("cookie") String userAgent,@FormParam("username") String username, @FormParam("password")String password){ //FormParam ce sont les parametres d'un formulaire. 
 
 
		User wantConnect=new User();
 
		if(username.length()>0 && password.length()>0){
			System.out.println("Both are positives" +"Username:"+username+" - Password:"+password);
			wantConnect = authenfication(username, password);
 
			if (wantConnect != null) {
				System.out.println(wantConnect);
				return Response.ok().build();
			}
			System.out.println(wantConnect);
		}
		return Response.ok()
	               .cookie(new NewCookie("name", username, "/", null,1,      
	                       "no comment",      
	                       1073741823 , // maxAge max int value/2      
	                       false ))
	               .build();
 
		//return wantConnect;
 
	}
	
	private User authenfication(String username, String password) {
		// TODO Auto-generated method stub
		User user=new User();
		user.setName(username);
		user.setPassword(password);
		//Split username and password tokens
//		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
//		final String username = tokenizer.nextToken();
//		final String password = tokenizer.nextToken();
		Cookie test = new Cookie("userTest",username);
		System.out.println(test.getName());
		return user;
	}

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{userID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getIt(@PathParam("userID") String userID) {
		return uManager.getUser(userID);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postUser(User user) throws URISyntaxException {
		if (uManager.userExist(user) == false) {
			User u=uManager.createUser(user);
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(u.getUserID())).build();
		} else {
			return Response.status(409).build();
		}

	}

	@PUT
	@Path("{userID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putUser(User user, @PathParam("userID") String userIDFromPath) {
		// TODO: need to check the authentication of the user
		// modify the user, check if the user has changed his email address, and check the ability of the new email address
		if ( user.getUserID().equals(userIDFromPath ) || uManager.userExist(user) == false ) {
			uManager.saveUser(user);
			return Response.status(200).build();
		} else {
			return Response.status(409).build();
		}

	}

	@DELETE
	@Path("{userID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteUser(@PathParam("userID") String userID) {
		// TODO: need to check the authentication of the user

		// delete the user
		uManager.deleteUser(userID);
		return Response.status(200).build();

	}

}
