package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
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
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

// The Java class will be hosted at the URI path "/app/account"
@Path("app/account")
public class UserEndPoints extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserEndPoints.class);
	AccountService uManager = new AccountServiceImpl(new UserRepositoryMongo("mediahome"));

	// Only for tests
	@GET
	@Path("get")
	//@RolesAllowed("account")
	public Response addUser(@Context HttpHeaders headers,@Context SecurityContext context) {//@HeaderParam("cookie") String userAgent) {
		
		String userAgent = headers.getRequestHeader("cookie").get(0);
		LOGGER.debug("userAgent : {}",userAgent);
		return Response.status(200)
			.entity("addUser is called, userAgent : " + userAgent)
			.build();
 
	}
	
	/**
	 * Create the cookie on user side when the user is connected
	 * @param userAgent
	 * @param username
	 * @param password
	 * @return
	 */
	@POST()
	@Path("Connect")
	@Produces({ "application/json"})// resultat en JSON
	public Response getConnect(User user){//@FormParam("username") String username, @FormParam("password")String password){ //FormParam ce sont les parametres d'un formulaire. 
		String username = user.getName().toLowerCase();

//		System.out.println("TEST"+username);
		String test = uManager.getUser(username).getUserID();
//		System.out.println("TEST" + test);
		if (uManager.userExist(uManager.getUser(username))) {
			User userAuth = uManager.getUser(username);
			if (user.getPassword().equals(userAuth.getPassword() ) ) {
				return Response.ok()
			               .cookie(new NewCookie("authentication", username, "/", null,1,      
			                       "no comment",      
			                       1073741823 , // maxAge max int value/2      
			                       false ))
			               .build();
				
			}
			return Response.status(403).build();
			
		}
		else{
			return Response.status(403).build();
		}
 
	}
	
	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{userID}")
	@RolesAllowed("account")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getIt(@PathParam("userID") String userID) {
		return uManager.getUser(userID);
	}
	
	/**
	 * Find a list of users from their name on server
	 * @param name
	 * @return a list of user
	 */
	@GET
	@Path("name/{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ListUser getUserFromName(@PathParam("name") String name){
		
		return uManager.getUserFromNameOnServer(name);
		
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postUser(User user) throws URISyntaxException {
		if (uManager.userExist(user) == false) {
			User u=uManager.createUser(user);
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(u.getUserID()))
		               .cookie(new NewCookie("authentication", u.getUserID(), "/", null,1,      
		                       "no comment",      
		                       1073741823 , // maxAge max int value/2      
		                       false ))
		               .build();
			//return Response.created(new URI(u.getUserID())).build();
		} else {
			return Response.status(409).build();
		}
	}

	@PUT
	@Path("{userID}")
	@RolesAllowed("account")
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
	@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteUser(@PathParam("userID") String userID) {
		// TODO: need to check the authentication of the user

		// delete the user
		uManager.deleteUser(userID);
		return Response.status(200).build();

	}

}
