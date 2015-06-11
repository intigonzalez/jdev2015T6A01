package com.enseirb.telecom.dngroup.snapmail.mail.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;
import com.enseirb.telecom.dngroup.snapmail.mail.Mailer;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;

public class GoogleMailer implements Mailer{
	
	private GoogleMailProperties googleMailProperties;
	
	public void send(MimeMessage message){
		String token = googleMailProperties.getTokenRefresh();
		Gmail service = null;
		
		try {
			service = getService(token);
			com.google.api.services.gmail.model.Message message2;
			message2 = createMessageWithEmail(message);
			message2 = service.users().messages().send("me", message2)
					.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public GoogleMailer(GoogleMailProperties googleMailProperties) {
		super();
		this.googleMailProperties = googleMailProperties;
	}
	
	/**
	 * Method to convert the MimeMessage to a "Google" Message wich can be sent via Gmail API
	 * 
	 * @param email
	 * @return Message
	 * @throws MessagingException
	 * @throws IOException
	 */
	private static com.google.api.services.gmail.model.Message createMessageWithEmail(
			MimeMessage email) throws MessagingException, IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		email.writeTo(bytes);
		// Encode the mail in base64 for the Gmail API
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes
				.toByteArray());
		com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
		message.setRaw(encodedEmail);
		return message;
	}
	private static GoogleCredential createCredentialWithRefreshToken(HttpTransport transport,
            JsonFactory jsonFactory, TokenResponse tokenResponse) throws FileNotFoundException, IOException {
		
		final String googleclient_ID = CliConfSingleton.google_clientID;
		final String googleclient_secret = CliConfSingleton.google_clientsecret;

       return new GoogleCredential.Builder().setTransport(transport)
              .setJsonFactory(jsonFactory)
              .setClientSecrets(googleclient_ID,googleclient_secret)
              .build()
              .setFromTokenResponse(tokenResponse);
        }
	
	/**
	 * Method to get the token thanks to our json secret. The user need to agree and copy-paste a link into the terminal.
	 * 
	 * @param token
	 * @return Gmail service
	 * @throws IOException
	 */
	private static Gmail getService(String token) throws IOException {

		// Link to give us rights to send mails
		// final String SCOPE = "https://www.googleapis.com/auth/gmail.compose";
		// Path to the client_secret.json file downloaded from the Developer
		// Console
		
		final String APP_NAME = "Snapmail";
		

		// Initialization
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		
        GoogleCredential credential = createCredentialWithRefreshToken(
                httpTransport, jsonFactory, new TokenResponse().setRefreshToken(token));
            credential.getAccessToken();

		// Create a new authorized Gmail API client and return it.
		Gmail service = new Gmail.Builder(httpTransport, jsonFactory,
				credential).setApplicationName(APP_NAME).build();
		return service;
	}
}
