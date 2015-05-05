package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.SmtpProperty;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;

// The Java class will be hosted at the URI path "/"

@Path("/")
public class SnapmailEndPoints extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserEndPoints.class);
	@Inject
	AccountService uManager;
		
	private static final String Googleclient_ID = CliConfSingleton.google_clientID;
	private static final String Googleclient_secret = CliConfSingleton.google_clientsecret; 
	private static final String Yahooclient_ID = CliConfSingleton.yahoo_clientID;
	private static final String Yahooclient_secret = CliConfSingleton.yahoo_clientsecret;
	private static final String Microsoftclient_ID = "000000004C14F710";
	private static final String Microsoftclient_secret = "nYBtVB-xkEUnVp3gZdkIMHu4DcAeGZPh";
	private static final String redirectUri = CliConfSingleton.centralURL.toString() + "/api/oauth";
	private static final String redirectUritest = "http://mathias.homeb.tv:9999/api/oauth"; 
	
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
		try {
			SmtpProperty smtpProperty = new SmtpProperty();
			User user = uManager.getUserOnLocal(actorIDFromPath);		
			smtpProperty.setHost(user.getSmtpHost());
			smtpProperty.setPort(user.getSmtpPort());
			smtpProperty.setUsername(user.getSmtpUsername());
			smtpProperty.setPassword(user.getSmtpPassword());
			smtpProperty.setToken(user.getSmtpToken());
			return smtpProperty;
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
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
		try {
			User user = uManager.getUserOnLocal(actorIDFromPath);
			
			user.setSmtpHost(smtpProperty.getHost());
			user.setSmtpPort(smtpProperty.getPort());
			user.setSmtpUsername(smtpProperty.getUsername());
			user.setSmtpPassword(smtpProperty.getPassword());
			user.setSmtpToken(smtpProperty.getToken());
			
			uManager.saveUserOnServer(user);
			return Response.status(200).build();
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	/**
	 * Redirect the client to google or yahoo identification and authorization system
	 * @param actorID
	 * @param service (google or yahoo)
	 * @throws URISyntaxException 
	 * @return 302 if the service is known, or 404
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
					+ "&redirect_uri=" + redirectUri
					+ "&scope=https://www.googleapis.com/auth/gmail.compose"
					+ "&state=" + actorID
					+ "&approval_prompt=force"
					+ "&access_type=offline"
					)).build();
		case "microsoft":
			return Response.seeOther(new URI(
					//"https://login.microsoftonline.com/common/oauth2/authorize"
					"https://login.live.com/oauth20_authorize.srf"
					+ "?response_type=code"
					+ "&client_id=" + Microsoftclient_ID
					+ "&redirect_uri=" + redirectUri
					+ "&scope=wl.offline_access,wl.imap"
					+ "&state=" + actorID
					+ "&access_type=offline"
					+ "&approval_prompt=force"
					)).build();
		case "yahoo":
			return Response.seeOther(new URI(
					"https://api.login.yahoo.com/oauth2/request_auth"
					+ "?response_type=code"
					+ "&client_id=" + Yahooclient_ID
					+ "&redirect_uri=" + redirectUri.replace(":9999","").replace(":8080", "")// Because yahoo refuse Uri with port
					+ "&state=" + actorID
					)).build();
		default:
			return Response.status(404).build();
		}
	}
	
	/**
	 * Request a refresh_token to google or yahoo thanks to the authorization code
	 * and save this token
	 * @param actorID
	 * @param code
	 * @throw URISyntaxException
	 * @return an error if google or yahoo don't give a token, a "ok" if the token is saved
	 */
	
	@POST
	@Path("oauth/{actorID}")
	@Consumes("text/plain")
	@SuppressWarnings("finally")
	public Response postOauth(@PathParam("actorID") String actorID, String code) throws URISyntaxException {
		
		
		Client client = ClientBuilder.newClient();
		String response="";
		String data;
		
// Send token request to google		
if(actorID.contains("@gmail.com")){
		WebTarget targetGoogle = client.target("https://www.googleapis.com/oauth2/v3/token");
		
		data = "client_id=" + Googleclient_ID
				+ "&client_secret=" + Googleclient_secret
				+ "&code=" + code
				+ "&redirect_uri=" + redirectUri
				+ "&grant_type=authorization_code"
				+ "&access_type=offline";
		
		response = targetGoogle
				.request()
				.post(Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		LOGGER.info(response.toString());
		
// Send token request to Yahoo		
/*}else if(actorID.contains("@yahoo."))
{
	WebTarget targetYahoo = client.target("https://api.login.yahoo.com/oauth2/get_token");
	
	String secure = Yahooclient_ID + ":" + Yahooclient_secret;
	String encodedvalue= Base64.getEncoder().encodeToString(secure.getBytes());

	data = "client_id=" + Yahooclient_ID
			+ "&client_secret=" + Yahooclient_secret
			+ "&code=" + code
			+ "&redirect_uri=" + redirectUri.replace(":9999","").replace(":8080", "") // Because yahoo refuse Uri with port
			+ "&grant_type=authorization_code";
			
	
	response = targetYahoo
			.request()
			.header("Authorization", "Basic " + encodedvalue)
			.post(Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED), String.class);
	LOGGER.info(response.toString());	
*/
} else {
	WebTarget targetOutlook = client.target("https://login.live.com/oauth20_token.srf");
	
	data = "client_id=" + Microsoftclient_ID
			+ "&client_secret=" + Microsoftclient_secret
			+ "&code=" + code
			+ "&redirect_uri=" + redirectUri
			+ "&grant_type=authorization_code";
			
	
	response = targetOutlook
			.request()
			.post(Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED), String.class);
	LOGGER.info(response.toString());
}
// get the refresh token
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
			// save the token
			Response responsePut;
		if (token.equals("")==false){	
			try {
				User user = uManager.getUserOnLocal(actorID);
				
				user.setSmtpHost("");
				user.setSmtpPort("");
				user.setSmtpUsername("");
				user.setSmtpPassword("");
				user.setSmtpToken(token);
				
				uManager.saveUserOnServer(user);
				
				responsePut =Response.status(200).build();
			} catch (NoSuchUserException e) {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		}
		else{
			responsePut=Response.status(500).build();
		}
		return responsePut;
		}
	}
}
