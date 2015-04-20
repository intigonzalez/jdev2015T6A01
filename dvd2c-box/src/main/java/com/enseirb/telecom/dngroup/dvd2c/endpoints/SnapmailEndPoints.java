package com.enseirb.telecom.dngroup.dvd2c.endpoints;

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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.model.SmtpProperty;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceImpl;

// The Java class will be hosted at the URI path "/"

@Path("/")
public class SnapmailEndPoints extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserEndPoints.class);
	AccountService uManager = new AccountServiceImpl(new UserRepositoryMongo("mediahome"));
	
	private static final String Googleclient_ID = "547107646254-uh9ism7k6qoho9jdcbg4v4rg4tt5pid0.apps.googleusercontent.com";
	private static final String Googleclient_secret = "JG3LiwiX2gA362mTSGEJ5eC8";
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
					+ "&client_id=" + Googleclient_ID
					+ "&redirect_uri=http://localhost:9999/api/oauth"
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
	
	@SuppressWarnings("finally")
	@POST
	@Path("oauth/{actorID}")
	//@RolesAllowed({ "other", "authenticated" })
	@Consumes("text/plain")
	public Response postOauth(@PathParam("actorID") String actorID, String code) throws URISyntaxException {
		
		// Requete google pour transformer le code en token
		// String code = "";
		
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target("https://www.googleapis.com/oauth2/v3/token");
		
		String data = "client_id=" + Googleclient_ID
				+ "&client_secret=" + Googleclient_secret
				+ "&code=" + code
				+ "&redirect_uri=http://localhost:9999/api/oauth"
				+ "&grant_type=authorization_code"
				+ "&access_type=offline";
		
		String response = target
				.request()
				.post(Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		LOGGER.info(response.toString());
		JSONObject json;
		String token="";
		try {
			json= new JSONObject(response);
			token=json.get("refresh_token").toString();
			LOGGER.info("Good Token");
		} catch (JSONException e) {
			
			LOGGER.error("Error with the token");
		}
		finally{
			Response responsePut;
		if (token.equals("")==false){
			Client clientPut = ClientBuilder.newClient();
			WebTarget targetPut = clientPut.target("http://localhost:9998/api/app/snapmail/" + actorID +"/smtp");
			SmtpProperty prop = new SmtpProperty();
			prop.setHost("");
			prop.setPassword("");
			prop.setPort("");
			prop.setUsername("");
			prop.setToken(token);
			
			responsePut = targetPut
					.request()
					.cookie("authentication", actorID)
					.put(Entity.entity(prop, MediaType.APPLICATION_XML), Response.class);
		}
		else{
			responsePut=Response.status(500).build();
		}
		
		return responsePut;
		}
	}
}
