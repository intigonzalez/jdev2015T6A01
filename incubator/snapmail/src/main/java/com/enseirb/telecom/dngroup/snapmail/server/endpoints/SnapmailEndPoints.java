package com.enseirb.telecom.dngroup.snapmail.server.endpoints;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.json.JSONException;
import org.json.JSONObject;

import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Property;
import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;

// The Java class will be hosted at the URI path "/"

@Path("/")
public class SnapmailEndPoints extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SnapmailEndPoints.class);

	private static final String Googleclient_ID = CliConfSingleton.google_clientID;
	private static final String Googleclient_secret = CliConfSingleton.google_clientsecret;
	private static final String Yahooclient_ID = CliConfSingleton.yahoo_clientID;
	private static final String Yahooclient_secret = CliConfSingleton.yahoo_clientsecret;
	private static final String Microsoftclient_ID = "000000004C14F710";
	private static final String Microsoftclient_secret = "nYBtVB-xkEUnVp3gZdkIMHu4DcAeGZPh";
	private static final String redirectUri = CliConfSingleton.centralURL
			.toString() + "/api/oauth";

	/**
	 * Send a request to the box to get a content
	 * 
	 * @param actorID
	 * @param contentID
	 * @return content
	 */
	@GET
	@Path("/app/{actorID}/content/{contentID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Content getBoxContent(@PathParam("actorID") String actorID,
			@PathParam("contentID") String contentID) {
		Content content;
		Client client = ClientBuilder.newClient();
		String url = CliConfSingleton.mediahome_host.toString() + "/api/app/"
				+ actorID + "/content/" + contentID;
		WebTarget target = client.target(url);
		content = target.request(MediaType.APPLICATION_XML_TYPE).get(
				Content.class);
		if (!content.getLink().contains(CliConfSingleton.mediahome_host)) {
			content.setLink(CliConfSingleton.mediahome_host + content.getLink());
		}
		return content;
	}

	/**
	 * Redirect the client to google or yahoo identification and authorization
	 * system
	 * 
	 * @param actorID
	 * @param service
	 *            (google or yahoo)
	 * @throws URISyntaxException
	 * @return 302 if the service is known, or 404
	 */
	@GET
	@Path("oauth/{actorID}/{service}")
	public Response getOauthredirect(@PathParam("actorID") String actorID,
			@PathParam("service") String service) throws URISyntaxException {
		switch (service) {
		case "google":
			return Response
					.seeOther(
							new URI(
									"https://accounts.google.com/o/oauth2/auth"
											+ "?response_type=code"
											+ "&client_id="
											+ Googleclient_ID
											+ "&redirect_uri="
											+ redirectUri
											+ "&scope=https://www.googleapis.com/auth/gmail.compose"
											+ "&state=" + actorID
											+ "&approval_prompt=force"
											+ "&access_type=offline")).build();
		case "microsoft":
			return Response.seeOther(
					new URI(
					// "https://login.microsoftonline.com/common/oauth2/authorize"
							"https://login.live.com/oauth20_authorize.srf"
									+ "?response_type=code" + "&client_id="
									+ Microsoftclient_ID + "&redirect_uri="
									+ redirectUri
									+ "&scope=wl.offline_access,wl.imap"
									+ "&state=" + actorID
									+ "&access_type=offline"
									+ "&approval_prompt=force")).build();
		case "yahoo":
			return Response.seeOther(
					new URI("https://api.login.yahoo.com/oauth2/request_auth"
							+ "?response_type=code"
							+ "&client_id="
							+ Yahooclient_ID
							+ "&redirect_uri="
							+ redirectUri.replace(":9999", "").replace(":8080",
									"")// Because yahoo refuse Uri with port
							+ "&state=" + actorID)).build();
		default:
			return Response.status(404).build();
		}
	}

	/**
	 * Request a refresh_token to google or yahoo thanks to the authorization
	 * code and save this token
	 * 
	 * @param actorID
	 * @param code
	 * @throw URISyntaxException
	 * @return an error if google or yahoo don't give a token, a "ok" if the
	 *         token is saved
	 */

	@POST
	@Path("oauth/{actorID}")
	@Consumes("text/plain")
	@SuppressWarnings("finally")
	public Response postOauth(@PathParam("actorID") String actorID, String code)
			throws URISyntaxException {

		Client client = ClientBuilder.newClient();
		String response = "";
		String data;
		PropertyGroups properties = new PropertyGroups();
		properties.setName("properties");

		Property property = new Property();

		// Send token request to google
		if (actorID.contains("@gmail.com")) {
			WebTarget targetGoogle = client
					.target("https://www.googleapis.com/oauth2/v3/token");

			data = "client_id=" + Googleclient_ID + "&client_secret="
					+ Googleclient_secret + "&code=" + code + "&redirect_uri="
					+ redirectUri + "&grant_type=authorization_code"
					+ "&access_type=offline" + "&approval_prompt=force";

			response = targetGoogle.request().post(
					Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED),
					String.class);
			LOGGER.info(response.toString());

			property.setKey("google");
			// Send token request to Yahoo
			// /*}else if(actorID.contains("@yahoo."))
			// {
			// WebTarget targetYahoo =
			// client.target("https://api.login.yahoo.com/oauth2/get_token");
			//
			// String secure = Yahooclient_ID + ":" + Yahooclient_secret;
			// String encodedvalue=
			// Base64.getEncoder().encodeToString(secure.getBytes());
			//
			// data = "client_id=" + Yahooclient_ID
			// + "&client_secret=" + Yahooclient_secret
			// + "&code=" + code
			// + "&redirect_uri=" +
			// redirectUri.replace(":9999","").replace(":8080", "") // Because
			// yahoo refuse Uri with port
			// + "&grant_type=authorization_code";
			//
			//
			// response = targetYahoo
			// .request()
			// .header("Authorization", "Basic " + encodedvalue)
			// .post(Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED),
			// String.class);
			// LOGGER.info(response.toString());
			// */
		} else {
			WebTarget targetOutlook = client
					.target("https://login.live.com/oauth20_token.srf");

			data = "client_id=" + Microsoftclient_ID + "&client_secret="
					+ Microsoftclient_secret + "&code=" + code
					+ "&redirect_uri=" + redirectUri
					+ "&grant_type=authorization_code";

			response = targetOutlook.request().post(
					Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED),
					String.class);
			LOGGER.info(response.toString());

			property.setKey("microsoft");
		}
		// get the refresh token
		JSONObject json;
		String token = "";
		try {
			json = new JSONObject(response);
			token = json.get("refresh_token").toString();
			LOGGER.info("Good Token");
		} catch (JSONException e) {

			LOGGER.error("Error with the token");
		} finally {
			// save the token
			Response responsePut = null;
			if (token.equals("") == false) {
				Client client1 = ClientBuilder.newClient();

				property.setValue(token);
				properties.getProperty().add(property);

				WebTarget target = client1
						.target(CliConfSingleton.mediahome_host
								+ "/api/app/account/" + actorID
								+ "/properties/snapmail");
				Response response1 = target
						.request(MediaType.APPLICATION_XML_TYPE)
						.cookie("authentication", actorID)
						.put(Entity.entity(properties,
								MediaType.APPLICATION_XML), Response.class);

				responsePut = response1;

			} else {
				responsePut = Response.status(500).build();
			}
			return responsePut;
		}
	}

	protected User getUser(String username) {
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(CliConfSingleton.mediahome_host
				+ "/api/app/account/" + username);
		User user = target.request(MediaType.APPLICATION_XML_TYPE)
				.cookie("authentication", username).get(User.class);
		return user;
	}

	@GET
	@PathParam("/api/app/login")
	Response logInMediaHome() throws URISyntaxException {
		return Response.seeOther(
				new URI(CliConfSingleton.mediahome_host + "home.html#"
						+ "/home")).build();
	}

}
