package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.SmtpProperty;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceImpl;
import com.google.common.io.Files;

// The Java class will be hosted at the URI path "/"

@Path("/")
public class SnapmailEndPoints extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserEndPoints.class);
	AccountService uManager = new AccountServiceImpl(new UserRepositoryMongo("mediahome"));
	
	/**
	 * Get the smtp properties from a user by actorID
	 * @param actorIDFromPath - the user
	 * @return a collection of smtp property
	 */
	@GET
	@Path("app/snapmail/{actorID}/smtp")
	@RolesAllowed({ "account", "authenticated" })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SmtpProperty getUserSmtpProperty(@PathParam("actorID") String actorIDFromPath){
		SmtpProperty smtpProperty = new SmtpProperty();
		User user = uManager.getUserOnLocal(actorIDFromPath);		
		smtpProperty.setHost(user.getSmtpHost());
		smtpProperty.setPort(user.getSmtpPort());
		smtpProperty.setUsername(user.getSmtpUsername());
		smtpProperty.setPassword(user.getSmtpPassword());
		smtpProperty.setToken(user.getSmtpToken());
		return smtpProperty;
	}
	
	/**
	 * Update User smtp property by actorID
	 * @param smtpProperty - the smtp property
	 * @param actorIDFromPath the userID to update
	 * @return webstatus
	 */
	@PUT
	@Path("app/snapmail/{actorID}/smtp")
	@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUserSmtpProperty(SmtpProperty smtpProperty, @PathParam("actorID") String actorIDFromPath) {
		User user = uManager.getUserOnLocal(actorIDFromPath);
		
		user.setSmtpHost(smtpProperty.getHost());
		user.setSmtpPort(smtpProperty.getPort());
		user.setSmtpUsername(smtpProperty.getUsername());
		user.setSmtpPassword(smtpProperty.getPassword());
		user.setSmtpToken(smtpProperty.getToken());
		
		uManager.saveUserOnServer(user);
		return Response.status(200).build();
	}
	
	/**
	 * Redirect the client to google api
	 * @throws URISyntaxException 
	 */
	@GET
	@Path("oauth/{actorID}/{service}")
	public Response getOauthredirect(@PathParam("actorID") String actorID, @PathParam("service") String service) throws URISyntaxException {
		switch(service) {
		case "google":
			return Response.seeOther(new URI(
					"https://accounts.google.com/o/oauth2/auth"
					+ "?response_type=code"
					+ "&client_id=547107646254-uh9ism7k6qoho9jdcbg4v4rg4tt5pid0.apps.googleusercontent.com"
					+ "&redirect_uri=urn:ietf:wg:oauth:2.0:oob"
					+ "&scope=https://www.googleapis.com/auth/gmail.compose"
					+ "&state=" + actorID
					+ "&access_type=offline"
					)).build();
		case "microsoft":
			return Response.seeOther(new URI("http://www.hotmail.fr")).build();
		default:
			return Response.status(404).build();
		}
	}
	
	@POST
	@Path("oauth/{ActorID}")
	//@RolesAllowed({ "other", "authenticated" })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postOauth(SmtpProperty smtpProperty) throws URISyntaxException {
		
		// Requete google pour transformer le code en token
		String code = "";
		
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target("https://www.googleapis.com/oauth2/v3/token");
		
		String data = "client_id=547107646254-uh9ism7k6qoho9jdcbg4v4rg4tt5pid0.apps.googleusercontent.com"
				+ "&client_secret=JG3LiwiX2gA362mTSGEJ5eC8"
				+ "&code=" + code
				+ "&redirect_uri=urn:ietf:wg:oauth:2.0:oob"
				+ "&grant_type=authorization_code"
				+ "&approval_promt=force"
				+ "&access_type=offline";
		
		Response response = target
				.request()
				.post(Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED), Response.class);
		
		return response;
	}
}
