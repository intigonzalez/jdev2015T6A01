package com.enseirb.telecom.dngroup.snapmail.mail.impl;

import java.security.Provider;
import java.security.Security;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.snapmail.OAuth2SaslClientFactory;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;
import com.enseirb.telecom.dngroup.snapmail.mail.Mailer;

public class MicrosoftMailer implements Mailer {
	// TODO...	
	private final static Logger LOGGER = LoggerFactory
	.getLogger(MicrosoftMailer.class);

	private MicrosoftMailProperties microsorfMailProperties;

	public void send(MimeMessage message) {
		String username="";
		try {
			username = message.getFrom().toString();
		} catch (MessagingException e1) {
			e1.printStackTrace();
		} 
		
		String refreshToken = microsorfMailProperties.getTokenRefresh();
		String token = getOutlookToken(refreshToken);

		final class OAuth2Provider extends Provider {
			private static final long serialVersionUID = 1L;

			public OAuth2Provider() {
				super("Google OAuth2 Provider", 1.0,
						"Provides the XOAUTH2 SASL Mechanism");
				put("SaslClientFactory.XOAUTH2",
						"com.enseirb.telecom.dngroup.snapmail.OAuth2SaslClientFactory");
			}
		}
		Session session = null;
		Transport tr = null;
		Properties properties = System.getProperties();
		Security.addProvider(new OAuth2Provider());
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.starttls.required", "true");
		properties.setProperty("mail.smtp.sasl.enable", "true");
		properties.setProperty("mail.smtp.sasl.mechanisms", "XOAUTH2");
		properties.setProperty(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, token);
		properties.setProperty("mail.smtp.host", "smtp-mail.outlook.com");
		properties.setProperty("mail.user", username);
		properties.setProperty("mail.smtp.port", "587");

		session = Session.getInstance(properties, null);
		session.setDebug(true);

		try {
			tr = session.getTransport("smtp");
			String emptyPassword = "";
			tr.connect("smtp-mail.outlook.com", 587, username, emptyPassword);
			tr.sendMessage(message, message.getAllRecipients());
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	

	public MicrosoftMailer(MicrosoftMailProperties microsorfMailProperties) {
		super();
		this.microsorfMailProperties = microsorfMailProperties;
	}
	
	
	private static String getOutlookToken(String token) {
		final String Microsoftclient_ID = "000000004C14F710";
		final String Microsoftclient_secret = "nYBtVB-xkEUnVp3gZdkIMHu4DcAeGZPh";
		final String redirectUri = CliConfSingleton.centralURL.toString() + "/api/oauth";
		
		Client client = ClientBuilder.newClient();
		String response;
		String data;
		
		
		/*String secure = Yahooclient_ID + ":" + Yahooclient_secret;
		String encodedvalue= java.util.Base64.getEncoder().encodeToString(secure.getBytes());
		*/

		WebTarget targetOutlook = client.target("https://login.live.com/oauth20_token.srf");
		
		data = "client_id=" + Microsoftclient_ID
				+ "&client_secret=" + Microsoftclient_secret
				+ "&refresh_token=" + token
				+ "&redirect_uri=" + redirectUri
				+ "&grant_type=refresh_token";			

		response = targetOutlook
				.request()
				//.header("Authorization", "Basic " + encodedvalue)
				.post(Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		LOGGER.info(response.toString());
		
		// Analysis of the response, get the access_token
		JSONObject json;
		String outlookToken="";
		
		try {
			json= new JSONObject(response);
			outlookToken=json.get("access_token").toString();
			LOGGER.info("Good Token");
		} catch (JSONException e) {
			LOGGER.error("Error with the token");
		}
		
		return outlookToken;
	}	

}
