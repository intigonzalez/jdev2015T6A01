package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.model.Properties;
import com.enseirb.telecom.dngroup.dvd2c.model.Property;
import com.enseirb.telecom.dngroup.dvd2c.model.SmtpProperty;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceImpl;

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
	public Response getConnect(User user){ //FormParam ce sont les parametres d'un formulaire. 
		String userID = user.getUserID().toLowerCase();
		String test = uManager.getUserOnLocal(userID).getUserID();
		if (uManager.userExistOnLocal(userID)) {
			User userAuth = uManager.getUserOnLocal(userID);
			if (user.getPassword().equals(userAuth.getPassword() ) ) {
				return Response.ok()
			               .cookie(new NewCookie("authentication", userID, "/", null,1,      
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

	

	/**
	 * Get a user by userID
	 * @param userID the user to get
	 * @return a user
	 */
	@GET
	@Path("{userID}")
	@RolesAllowed("account")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUserByID(@PathParam("userID") String userID) {
		return uManager.getUserOnLocal(userID);
	}
	
	/**
	 * Find a list of users from their firstname on server
	 * @param firstname the firstname to search
	 * @return a list of user
	 */
	@GET
	@Path("firstname/{firstname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<User> getUserByName(@PathParam("firstname") String firstname){
		return uManager.getUserFromNameOnServer(firstname);
	}

	/**
	 * Create a user on server and local db
	 * @param user the user to save
	 * @return Status web
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createUser(User user) throws URISyntaxException {
		if (uManager.userExistOnLocal(user.getUserID()) == false) {
			user.setBoxID(CliConfSingleton.boxID);
			User u=uManager.createUserOnServer(user);
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

	/**
	 * Update User by userID
	 * @param user the user information 
	 * @param userIDFromPath the userID to update
	 * @return webstatus
	 */
	@PUT
	@Path("{userID}")
	@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUser(User user, @PathParam("userID") String userIDFromPath) {
		// TODO: need to check the authentication of the user
		// modify the user, check if the user has changed his email address, and check the ability of the new email address
		if ( user.getUserID().equals(userIDFromPath ) || uManager.userExistOnLocal(user.getUserID()) == false ) {
			uManager.saveUserOnServer(user);
			return Response.status(200).build();
		} else {
			return Response.status(409).build();
		}

	}

	/**
	 * Delete the user with userID
	 * @param userID the userID to delete
	 * @return web status
	 */
	@DELETE
	@Path("{userID}")
	@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteUser(@PathParam("userID") String userID) {
		// TODO: need to check the authentication of the user

		// delete the user
		uManager.deleteUserOnServer(userID);
		return Response.status(200).build();

	}
	
	/**
	 * Get the smtp properties from a user by actorID
	 * @param actorIDFromPath - the user
	 * @return a collection of smtp property
	 */
	@GET
	@Path("{actorID}/smtp")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SmtpProperty getUserSmtpProperty(@PathParam("actorID") String actorIDFromPath){
		SmtpProperty smtpProperty = new SmtpProperty();
		User user = uManager.getUserOnLocal(actorIDFromPath);
		
		smtpProperty.setHost(user.getSmtpHost());
		smtpProperty.setPort(user.getSmtpPort());
		smtpProperty.setUsername(user.getSmtpUsername());
		smtpProperty.setPassword(user.getSmtpPassword());
		return smtpProperty;
	}
	
	/**
	 * Update User smtp property by actorID
	 * @param smtpProperty - the smtp property
	 * @param actorIDFromPath the userID to update
	 * @return webstatus
	 */
	@PUT
	@Path("{actorID}/smtp")
	//@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUserSmtpProperty(SmtpProperty smtpProperty, @PathParam("actorID") String actorIDFromPath) {
		User user = uManager.getUserOnLocal(actorIDFromPath);
		
		user.setSmtpHost(smtpProperty.getHost());
		user.setSmtpPort(smtpProperty.getPort());
		user.setSmtpUsername(smtpProperty.getUsername());
		user.setSmtpPassword(smtpProperty.getPassword());
		
		uManager.saveUserOnServer(user);
		return Response.status(200).build();
	}
	
	/**
	 * Get the smtp properties from a user by actorID
	 * @param actorIDFromPath - the user
	 * @return a collection of smtp property
	 */
	@GET
	@Path("{actorID}/smtpDev")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Collection<SmtpProperty> getUserSmtpPropertyDev(@PathParam("actorID") String actorIDFromPath){
		return uManager.getUserProperty(actorIDFromPath, SmtpProperty.class);
	}
	
	/**
	 * Update User smtp property by actorID
	 * @param smtpProperty - the smtp property
	 * @param actorIDFromPath the userID to update
	 * @return webstatus
	 */
	@PUT
	@Path("{actorID}/smtpDev")
	//@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUserSmtpPropertyDev(SmtpProperty smtpProperty, @PathParam("actorID") String actorIDFromPath) {
		// TODO: need to check the authentication of the user
		uManager.saveUserProperty(actorIDFromPath, smtpProperty);
		return Response.status(200).build();
	}
}
