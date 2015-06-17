package com.enseirb.telecom.dngroup.snapmail.server.service;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.model.Property;
import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;
import com.enseirb.telecom.dngroup.snapmail.server.endpoints.SnapmailEndPoints;

@Service
public class SnapmailServiceImpl implements SnapmailService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SnapmailEndPoints.class);

	@Override
	public Response redirectOauthService(String service, String actorID) {
		final String Googleclient_ID = CliConfSingleton.google_clientID;
		final String Yahooclient_ID = CliConfSingleton.yahoo_clientID;
		final String Microsoftclient_ID = "000000004C14F710";
		final String redirectUri = CliConfSingleton.centralURL
				.toString() + "/api/oauth";
		switch (service) {

		case "google":
			try {
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
												+ "&access_type=offline"))
						.build();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

		case "microsoft":
			try {
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
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

		case "yahoo":
			try {
				return Response.seeOther(
						new URI(
								"https://api.login.yahoo.com/oauth2/request_auth"
										+ "?response_type=code"
										+ "&client_id="
										+ Yahooclient_ID
										+ "&redirect_uri="
										+ redirectUri.replace(":9999", "")
												.replace(":8080", "")// Because
																		// yahoo
																		// refuse
																		// Uri
																		// with
																		// port
										+ "&state=" + actorID)).build();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

		default:
			return Response.status(404).build();
		}
	}

	@Override
	public Response getOauthTokenWithCode(String service, String code,
			String userID, String validator) {
		final String Googleclient_ID = CliConfSingleton.google_clientID;
		final String Googleclient_secret = CliConfSingleton.google_clientsecret;
		final String Yahooclient_ID = CliConfSingleton.yahoo_clientID;
		final String Yahooclient_secret = CliConfSingleton.yahoo_clientsecret;
		final String Microsoftclient_ID = "000000004C14F710";
		final String Microsoftclient_secret = "nYBtVB-xkEUnVp3gZdkIMHu4DcAeGZPh";
		final String redirectUri = CliConfSingleton.centralURL
				.toString() + "/api/oauth";
		
		Client client = ClientBuilder.newClient();
		String response = "";
		String data;
		PropertyGroups properties = new PropertyGroups();
		properties.setName("Snapmail");

		Property property = new Property();

		switch (service) {
		case "google":
			// Send token request to google
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
			break;
		case "yahoo":
			// Send token request to Yahoo
			// }else if(actorID.contains("@yahoo."))
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
			//
			break;

		case "microsoft":
			WebTarget targetOutlook = client
					.target("https://login.live.com/oauth20_token.srf");

			data = "client_id=" + Microsoftclient_ID + "&client_secret="
					+ Microsoftclient_secret + "&code=" + code
					+ "&redirect_uri=" + redirectUri
					+ "&grant_type=authorization_code";

			response = targetOutlook.request().post(
					Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED),
					String.class);

			property.setKey("microsoft");
			break;

		default:
			client.close();
			return Response.status(500).build();
		}
		client.close();

		// get the refresh token
		JSONObject json;
		String token = "";
		try {
			json = new JSONObject(response);
			token = json.get("refresh_token").toString();
			LOGGER.info("Good Token");
		} catch (JSONException e) {

			LOGGER.error("Error with the token");
		}
		// save the token
		Response responsePut = null;
		if (token.equals("") == false) {
			Client client1 = ClientBuilder.newClient();

			property.setValue(token);
			properties.getProperty().add(property);

			// TODO: à modifier quand le problème de sécurité sera réglé
			WebTarget target = client1.target(CliConfSingleton.mediahome_host
					+ "/api/snapmail/properties/" + userID + "/" + validator);
			Response response1 = target.request(MediaType.APPLICATION_XML_TYPE)
					.put(Entity.entity(properties, MediaType.APPLICATION_XML),
							Response.class);

			responsePut = response1;
			client1.close();

		} else {
			responsePut = Response.status(500).build();
		}
		return responsePut;
	}

}
