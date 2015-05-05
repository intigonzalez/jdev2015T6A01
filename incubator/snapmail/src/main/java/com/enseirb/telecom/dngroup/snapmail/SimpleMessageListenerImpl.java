package com.enseirb.telecom.dngroup.snapmail;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.auth.UsernamePasswordValidator;
import org.subethamail.smtp.helper.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.util.SharedFileInputStream;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.TooMuchDataException;

import com.enseirb.telecom.dngroup.dvd2c.model.SmtpProperty;
import com.philvarner.clamavj.ClamScan;
import com.philvarner.clamavj.ScanResult;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;

public class SimpleMessageListenerImpl implements SimpleMessageListener,
		UsernamePasswordValidator {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SimpleMessageListener.class);

	protected String username;
	protected String password;

	// to display all recipients
	ArrayList<String> recipientArray = new ArrayList<String>();
	private String allrecipients;
	private int counter = 0;

	// to certify that recipients are real adresses
	private int position = 0;

	MessageContext context;
	private String subject;
	private String text;
	private String type;
	private InputStream data;

	// private final static Logger LOGGER =
	// Logger.getLogger(SimpleMessageListenerImpl.class.getName());
	
	private String Clamav_report = "\n----------- ClamAV -----------------\n----------- SCAN SUMMARY -----------\n";
	private boolean mail_infected = false;
	
	@Override
	public void login(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public boolean accept(String from, String recipient) {
		position = recipient.indexOf("@");
		if (position != -1) {// If the address is correct mail address
			recipientArray.add("" + recipient);
			counter++;
		}
		return true;
	}
// Receive and deliver the mail
	@Override
	public void deliver(String from, String recipient, InputStream data)
			throws TooMuchDataException, IOException {
		// Emptying the array
		counter--;
		if (counter == 0) {
			allrecipients = recipientArray.toString();
			this.allrecipients = this.allrecipients.substring(1,
					this.allrecipients.length() - 1);
			LOGGER.info("\n--------------------------------------------------");
			LOGGER.info("FROM: " + from + "\n TO: " + allrecipients);
			// data
			this.data = data;
						
			Properties props = new Properties();
			Session session = Session.getInstance(props, null);

			// Creation of a new MIME Message
			MimeMessage newMessage = null;
			Multipart multiPart = new MimeMultipart();
			
			try {
				// The mail received as a stream of data by the app is stored into the MIME message
				File mimeFile = saveFile("temp_mime", data);
				
		        SharedFileInputStream fis = new SharedFileInputStream(mimeFile);
				newMessage = new MimeMessage(session, fis);
				mimeFile.delete();

				this.subject = newMessage.getSubject();
				String contentType = newMessage.getContentType();
				this.type = contentType;
				Boolean isMultipart = contentType.contains("multipart");

				// Returns the text of the message
				this.text = getTextFromMessage(newMessage);

				LOGGER.info("Text : \n\t" + this.text);

				// If the message contains an attachment it is parsed to upload
				// the file on the cloud
				if (isMultipart) {
					LOGGER.info("This mail is multipart");
					parseMessage(newMessage,multiPart);
					
				}
			} catch (MessagingException e1) {
				
				e1.printStackTrace();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
		
		try {
			sendMail("smtp.gmail.com", recipient, multiPart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recipientArray.clear();

		// If infected send an email to the sender
		Clamav_report += "------------------------------------\nClamAV Antivirus is an open source antivirus engine for detecting trojans, viruses,malware & other malicious threats\n------------------------------------";
		if (mail_infected){
			sendClamavReport("smtp.gmail.com", from, Clamav_report);
			mail_infected = false;
		}
		Clamav_report = "\n----------- ClamAV -----------------\n----------- SCAN SUMMARY -----------\n";
		}
	}

	/**
	 * Rebuild and send mails, using the SMTPProperties of the sender
	 * 
	 * @param host
	 * @param recipient
	 * @param multiPart
	 * @throws IOException
	 * @throws MessagingException 
	 */
	private void sendMail(String host, String recipient, Multipart multiPart)
			throws IOException, MessagingException {
		// Get system properties
		Properties properties = System.getProperties();

		properties = setSMTPProperties(properties);
		Session session;
		Transport tr = null;
		String token = properties.getProperty("mail.token");
		if (token.equals("")) {
			final String pwd = properties.getProperty("mail.password");
			final String usr = properties.getProperty("mail.user");
			// These properties will change based on the remote smtp server used

			// Get the default Session object.
			session = Session.getInstance(properties,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							// Remote SMTP credentials
							return new PasswordAuthentication(usr, pwd);
						}
					});
		} else if(username.contains("@gmail.com")){
			session = Session.getInstance(properties);
		} else{
			String outlookToken= getOutlookToken(token);
			LOGGER.info("outlookToken : " + outlookToken);
			String secure = "user={" + this.username + "}\1auth=Bearer {" + outlookToken + "}\1\1";
			String encodedvalue= java.util.Base64.getEncoder().encodeToString(secure.getBytes());
			properties.setProperty("mail.smtp.auth", "true");
			properties.setProperty("mail.smtp.starttls.enable", "true");
		    properties.setProperty("mail.smtp.starttls.required", "true");
		    properties.setProperty("mail.smtp.sasl.mechanisms", "XOAUTH2");
		    properties.setProperty("mail.smtp.xoauth2", encodedvalue);
			properties.setProperty("mail.smtp.host", "smtp-mail.outlook.com");
			
			properties.setProperty("mail.user", this.username); 
			properties.setProperty("mail.smtp.port", "587");
			
			session = Session.getInstance(properties);
			tr = session.getTransport("smtp");
		    tr.connect("smtp-mail.outlook.com", encodedvalue);	
		}

		try {
			// Creation of the message that will be send in place of the
			// received mail
			// The attachments are removed and replaced by links which redirect
			// to the file stored in the cloud
			//
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(username));
			// message.setHeader("Content-Type", this.type);
			LOGGER.info("CONTENT-TYPE : " + this.type);
			// Set To: header field of the header.
			message.addRecipients(Message.RecipientType.TO, allrecipients);
			// Set Subject: header field
			message.setSubject(this.subject);
			// Now set the actual message
			// message.setContent(this.text, this.type);
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(this.text, this.type);
			multiPart.addBodyPart(textPart);
			message.setContent(multiPart);
			LOGGER.info("Mail rebuilt and ready to be sent");
			if (token.equals("")) {
				Transport.send(message);
			} else if(username.contains("@gmail.com")){
				Gmail service = getService(token);
				com.google.api.services.gmail.model.Message message2 = createMessageWithEmail(message);
				message2 = service.users().messages().send("me", message2)
						.execute();
			}
			else{
				
				tr.sendMessage(message, message.getAllRecipients());
				
			}
			LOGGER.info("Mail sent successfully !");
			LOGGER.info("--------------------------------------------------\n\n");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
	/**
	 * Send a mail to the user when a file is infected
	 * 
	 * @param host
	 * @param from
	 * @param Clamav_report
	 * @throws IOException
	 */
	private void sendClamavReport(String host, String from, String Clamav_report) throws IOException
	{
		// Get system properties
				Properties properties = System.getProperties();

				properties = setSMTPProperties(properties);
				Session session;
				String token = properties.getProperty("mail.token");
				if (token.equals("")) {
					final String pwd = properties.getProperty("mail.password");
					final String usr = properties.getProperty("mail.user");
					// These properties will change based on the remote smtp server used

					// Get the default Session object.
					session = Session.getInstance(properties,
							new javax.mail.Authenticator() {
								protected PasswordAuthentication getPasswordAuthentication() {
									// Remote SMTP credentials
									return new PasswordAuthentication(usr, pwd);
								}
							});
				} else
					session = Session.getInstance(properties);

		
		try {
			MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("ClamAV@snapmail.com", "INFO Snapmail"));
	        message.addRecipients(Message.RecipientType.TO,from);
	        message.setSubject("ClamAV Scan Report");
	        message.setContent(Clamav_report, "text/plain");
	        if (token.equals("")) {
				Transport.send(message);
			} else {
				Gmail service = getService(token);
				com.google.api.services.gmail.model.Message message2 = createMessageWithEmail(message);
				message2 = service.users().messages().send("me", message2)
						.execute();
			}
			LOGGER.info("ClamAV Report sent successfully !\n\n");
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
	/**
	 * Parse the message between the text and the attachment part, and scan it with clamAV
	 * 
	 * @param message
	 * @param multiPart
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void parseMessage(Message message, Multipart multiPart) throws MessagingException, IOException {
		this.text += "\n------------------------------------\n";
		// Since the message is multipart, it can be casted as such
		Multipart multipart = (Multipart) message.getContent();

		// Iteration through the different parts of the message
		for (int j = 0; j < multipart.getCount(); j++) {
			BodyPart bodyPart = multipart.getBodyPart(j);
			// A signed email will put the attachments into a MIME multipart,
			// thus if a part is a MIME multipart it is likely to contain
			// attachments
			if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
				// If the part is not an attachment, but a MimeMultipart...
				if (bodyPart.getContent().getClass()
						.equals(MimeMultipart.class)) {
					MimeMultipart mimemultipart = (MimeMultipart) bodyPart
							.getContent();
					// Iteration through the different parts of the MIME
					// multipart
					for (int k = 0; k < mimemultipart.getCount(); k++) {
						if (mimemultipart.getBodyPart(k).getContentType()
								.indexOf("text") == -1
								&& mimemultipart.getBodyPart(k)
										.getContentType().indexOf("multipart") == -1) {
							if (Part.INLINE != null
									| mimemultipart.getBodyPart(k).getHeader(
											"Content-ID")[0] != null) {// if
																		// attachment
																		// +
																		// embedded
																		// picture
																		// then
																		// the
																		// inline
																		// disposition
																		// is
																		// not
																		// writen
								LOGGER.info("embedded picture");
								try {
									//Scan viruses,trojans & malware for INLINE Pictures in MimemMultipart
									LOGGER.info("-----------------------------------------------------------");
									LOGGER.info("Starting ClamAv Scan for file: "+mimemultipart.getBodyPart(k).getFileName());
									ClamScan clamScan = new ClamScan(CliConfSingleton.clamav_host, Integer.parseInt(CliConfSingleton.clamav_port), 0);
									ScanResult result = clamScan.scan(mimemultipart.getBodyPart(k).getInputStream());
									LOGGER.info("ClamAv Scan Done !");
									if ( (result.getStatus().toString()).equals("FAILED")){
										mail_infected = true;
										LOGGER.info("The file is infected !");
										LOGGER.info("-----------------------------------------------------------");
										Clamav_report += "---\nFile infected : "+mimemultipart.getBodyPart(k).getFileName()+"\nSignature : "+result.getSignature()+"\nData Size : "+mimemultipart.getBodyPart(k).getSize()+"\nDescription : "+mimemultipart.getBodyPart(k).getDescription()+"\nContent-Type : "+mimemultipart.getBodyPart(k).getContentType()+"\n---\n";
									}
									else{
										LOGGER.info("No virus found in the file !");
										LOGGER.info("-----------------------------------------------------------");
										//add picture inline hmtl part
										DataSource ds = new ByteArrayDataSource(mimemultipart.getBodyPart(k).getInputStream(), mimemultipart.getBodyPart(k).getContentType());
										MimeBodyPart imagePart = new MimeBodyPart();
									    imagePart.setDataHandler(new DataHandler(ds));
									    if (mimemultipart.getBodyPart(k).getFileName() != null)
									    imagePart.setFileName(mimemultipart.getBodyPart(k).getFileName());
									    if (mimemultipart.getBodyPart(k).getFileName() != null)
									    imagePart.setDisposition(MimeBodyPart.INLINE);
									    imagePart.setHeader("Content-ID", mimemultipart.getBodyPart(k).getHeader("Content-ID")[0]);
									    multiPart.addBodyPart(imagePart);
									    this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
									}
								} catch (IOException clam) {
									LOGGER.info("-----------------------------------------------------------");
									//add picture inline hmtl part
									DataSource ds = new ByteArrayDataSource(mimemultipart.getBodyPart(k).getInputStream(), mimemultipart.getBodyPart(k).getContentType());
									MimeBodyPart imagePart = new MimeBodyPart();
								    imagePart.setDataHandler(new DataHandler(ds));
								    if (mimemultipart.getBodyPart(k).getFileName() != null)
								    imagePart.setFileName(mimemultipart.getBodyPart(k).getFileName());
								    if (mimemultipart.getBodyPart(k).getFileName() != null)
								    imagePart.setDisposition(MimeBodyPart.INLINE);
								    imagePart.setHeader("Content-ID", mimemultipart.getBodyPart(k).getHeader("Content-ID")[0]);
								    multiPart.addBodyPart(imagePart);
								    this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";
								}
							}
							else if (Part.ATTACHMENT != null){
								try {
									//Scan viruses,trojans & malware for ATTACHMENT in MimemMultipart
									LOGGER.info("embedded picture");
									//Scan viruses,trojans & malware for INLINE PICTURES
									LOGGER.info("-----------------------------------------------------------");
									LOGGER.info("Starting ClamAv Scan for file: "+mimemultipart.getBodyPart(k).getFileName());
									ClamScan clamScan = new ClamScan(CliConfSingleton.clamav_host, Integer.parseInt(CliConfSingleton.clamav_port), 0);
									ScanResult result = clamScan.scan(mimemultipart.getBodyPart(k).getInputStream());
									LOGGER.info("ClamAv Scan Done !");
									if ( (result.getStatus().toString()).equals("FAILED")){
										mail_infected = true;
										LOGGER.info("The file is infected !");
										LOGGER.info("-----------------------------------------------------------");
										Clamav_report += "---\nFile infected : "+mimemultipart.getBodyPart(k).getFileName()+"\nSignature : "+result.getSignature()+"\nData Size : "+mimemultipart.getBodyPart(k).getSize()+"\nDescription : "+mimemultipart.getBodyPart(k).getDescription()+"\nContent-Type : "+mimemultipart.getBodyPart(k).getContentType()+"\n---\n";
									}
									else{
										LOGGER.info("No virus found in the file !");
										LOGGER.info("-----------------------------------------------------------");
										processAttachment(mimemultipart.getBodyPart(k).getFileName(),mimemultipart.getBodyPart(k).getInputStream(),mimemultipart.getBodyPart(k).getContentType().substring(0, bodyPart.getContentType().indexOf(";")));
										this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
									}
								} catch (IOException clam) {
									LOGGER.info("-----------------------------------------------------------");
									processAttachment(mimemultipart.getBodyPart(k).getFileName(),mimemultipart.getBodyPart(k).getInputStream(),mimemultipart.getBodyPart(k).getContentType().substring(0, bodyPart.getContentType().indexOf(";")));
									this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";
								}
							}
						}
						// if there is another mimemultipart
						if (mimemultipart.getBodyPart(k).getContentType()
								.indexOf("multipart") != -1) {
							MimeMultipart mimemultipart_bis = (MimeMultipart) mimemultipart
									.getBodyPart(k).getContent();
							// Iteration through the different parts of the MIME
							// multipart
							for (int l = 0; l < mimemultipart_bis.getCount(); l++) {
								if (mimemultipart_bis.getBodyPart(l)
										.getContentType().indexOf("text") == -1) {
									if (Part.INLINE != null
											| mimemultipart_bis.getBodyPart(l)
													.getHeader("Content-ID")[0] != null) {// if
																							// attachment
																							// +
																							// embedded
																							// picture
																							// then
																							// the
																							// inline
																							// disposition
																							// is
																							// not
																							// wrote
										LOGGER.info("embedded picture");
										try {
											//Scan viruses,trojans & malware for INLINE PICTURES
											LOGGER.info("-----------------------------------------------------------");
											LOGGER.info("Starting ClamAv Scan for file: "+mimemultipart_bis.getBodyPart(l).getFileName());
											ClamScan clamScan = new ClamScan(CliConfSingleton.clamav_host, Integer.parseInt(CliConfSingleton.clamav_port), 0);
											ScanResult result = clamScan.scan(mimemultipart_bis.getBodyPart(l).getInputStream());
											LOGGER.info("ClamAv Scan Done !");
											if ( (result.getStatus().toString()).equals("FAILED")){
												mail_infected = true;
												LOGGER.info("The file is infected !");
												LOGGER.info("-----------------------------------------------------------");
												Clamav_report += "---\nFile infected : "+mimemultipart.getBodyPart(l).getFileName()+"\nSignature : "+result.getSignature()+"\nData Size : "+mimemultipart.getBodyPart(l).getSize()+"\nDescription : "+mimemultipart.getBodyPart(l).getDescription()+"\nContent-Type : "+mimemultipart.getBodyPart(l).getContentType()+"\n---\n";
											}
											else{
												LOGGER.info("No virus found in the file !");
												LOGGER.info("-----------------------------------------------------------");
												//add picture inline hmtl part
												DataSource ds = new ByteArrayDataSource(mimemultipart_bis.getBodyPart(l).getInputStream(), mimemultipart_bis.getBodyPart(l).getContentType());
												MimeBodyPart imagePart = new MimeBodyPart();
												imagePart.setDataHandler(new DataHandler(ds));
												if (mimemultipart_bis.getBodyPart(l).getFileName() != null)
													imagePart.setFileName(mimemultipart_bis.getBodyPart(l).getFileName());
												if (mimemultipart_bis.getBodyPart(l).getFileName() != null)
													imagePart.setDisposition(MimeBodyPart.INLINE);
												imagePart.setHeader("Content-ID", mimemultipart_bis.getBodyPart(l).getHeader("Content-ID")[0]);
												multiPart.addBodyPart(imagePart);
												this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
											}
										} catch (IOException clam) {
											
											LOGGER.info("-----------------------------------------------------------");
											//add picture inline hmtl part
											DataSource ds = new ByteArrayDataSource(mimemultipart_bis.getBodyPart(l).getInputStream(), mimemultipart_bis.getBodyPart(l).getContentType());
											MimeBodyPart imagePart = new MimeBodyPart();
											imagePart.setDataHandler(new DataHandler(ds));
											if (mimemultipart_bis.getBodyPart(l).getFileName() != null)
												imagePart.setFileName(mimemultipart_bis.getBodyPart(l).getFileName());
											if (mimemultipart_bis.getBodyPart(l).getFileName() != null)
												imagePart.setDisposition(MimeBodyPart.INLINE);
											imagePart.setHeader("Content-ID", mimemultipart_bis.getBodyPart(l).getHeader("Content-ID")[0]);
											multiPart.addBodyPart(imagePart);
											this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";
										}
									}
									else if (Part.ATTACHMENT != null){
										try {
											//Scan viruses,trojans & malware for ATTACHMENTS IN MIMEMULTIPART
											LOGGER.info("-----------------------------------------------------------");
											LOGGER.info("Starting ClamAv Scan for file: "+mimemultipart_bis.getBodyPart(l).getFileName());
											ClamScan clamScan = new ClamScan(CliConfSingleton.clamav_host, Integer.parseInt(CliConfSingleton.clamav_port), 0);
											ScanResult result = clamScan.scan(mimemultipart_bis.getBodyPart(l).getInputStream());	
											LOGGER.info("ClamAv Scan Done !");
											if ( (result.getStatus().toString()).equals("FAILED")){
												mail_infected = true;
												LOGGER.info("The file is infected !");
												LOGGER.info("-----------------------------------------------------------");
												Clamav_report += "---\nFile infected : "+mimemultipart.getBodyPart(l).getFileName()+"\nSignature : "+result.getSignature()+"\nData Size : "+mimemultipart.getBodyPart(l).getSize()+"\nDescription : "+mimemultipart.getBodyPart(l).getDescription()+"\nContent-Type : "+mimemultipart.getBodyPart(l).getContentType()+"\n---\n";
											}
											else{
												LOGGER.info("No virus found in the file !");
												LOGGER.info("-----------------------------------------------------------");
												processAttachment(mimemultipart_bis.getBodyPart(l).getFileName(),mimemultipart_bis.getBodyPart(l).getInputStream(),mimemultipart_bis.getBodyPart(l).getContentType().substring(0, bodyPart.getContentType().indexOf(";")));
												this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
											}
										} catch (IOException clam) {
											LOGGER.info("ClamAV isn't working");
											LOGGER.info("-----------------------------------------------------------");
											processAttachment(mimemultipart_bis.getBodyPart(l).getFileName(),mimemultipart_bis.getBodyPart(l).getInputStream(),mimemultipart_bis.getBodyPart(l).getContentType().substring(0, bodyPart.getContentType().indexOf(";")));
											this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";
											
										}
									}
								}
							}
						}
					}
				}
				continue;
			}
			
			try {
				//Scan viruses,trojans & malware for ATTACHMENTS
				LOGGER.info("-----------------------------------------------------------");
				LOGGER.info("Starting ClamAv Scan for file: "+bodyPart.getFileName());
				ClamScan clamScan = new ClamScan(CliConfSingleton.clamav_host, Integer.parseInt(CliConfSingleton.clamav_port), 0);
				// other solution : Cast inputstream to a FileInputstream
				// other solution : Input Stream into Byte Array
				ScanResult result = clamScan.scan(bodyPart.getInputStream());
				LOGGER.info("ClamAv Scan Done !");

				if ( (result.getStatus().toString()).equals("FAILED")){
					mail_infected = true;
					LOGGER.info("The file is infected !");
					LOGGER.info("-----------------------------------------------------------");
					Clamav_report += "---\nFile infected : "+bodyPart.getFileName()+"\nSignature : "+result.getSignature()+"\nData Size : "+bodyPart.getSize()+"\nDescription : "+bodyPart.getDescription()+"\nContent-Type : "+bodyPart.getContentType()+"\n---\n";
				}
				else{
					LOGGER.info("No virus found in the file !");
					LOGGER.info("-----------------------------------------------------------");
					// If the current part is explicitly an attachment...
					String contentType = bodyPart.getContentType().substring(0, bodyPart.getContentType().indexOf(";"));
					processAttachment(bodyPart.getFileName(), bodyPart.getInputStream(), contentType);
					LOGGER.info("Content type : "+ contentType);
					this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
				}
			} catch (IOException clam) {
				LOGGER.info("ClamAV isn't working");
				LOGGER.info("-----------------------------------------------------------");
				String contentType = bodyPart.getContentType().substring(0, bodyPart.getContentType().indexOf(";"));
				processAttachment(bodyPart.getFileName(), bodyPart.getInputStream(), contentType);
				LOGGER.info("Content type : "+ contentType);
				this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";
				
			}			
		}
	}

	/**
	 * Modify the mail and upload the attachment
	 * 
	 * @param filename
	 * @param is
	 * @param Type
	 * @throws IOException
	 */
	private void processAttachment(String filename, InputStream is, String Type)
			throws IOException {
		this.text += "Attachment : " + filename + "\n";
		String link = postFile(is, filename, Type);
		this.text += "Link :" + link + "\n";
	}

	/**
	 * Get the text part from the mail
	 * 
	 * @param message
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private String getTextFromMessage(Part message) throws MessagingException,
			IOException {
		String s = new String();
		this.type = message.getContentType();

		// If the type is text/html or text/plain, the text is extracted
		// directly
		if (message.isMimeType("text/*")) {
			s = (String) message.getContent();
			return s;
		}

		// Each part of a multipart/alternative type has the same content as the
		// others,
		// but in a different format
		if (message.isMimeType("multipart/alternative")) {
			Multipart mp = (Multipart) message.getContent();
			String text = null;

			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);

				// If the type of this part is "text/plain" or "text/html",
				// the algorithm is used recursively on this specific part
				// in order to find the text inside it
				if (bp.isMimeType("text/plain")) {
					// The text stored in the part of type "text/plain"
					// is kept just in case nothing better is found
					// The priority is put on the type "text/html"
					if (text == null)
						text = getTextFromMessage(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					s = getTextFromMessage(bp);
					if (s != null)
						return s;
				}
				// If the type is neither "text/html" nor "text/plain",
				// the algorithm is used recursively on this specific part
				else {
					return getTextFromMessage(bp);
				}
			}
			return text;
		} else if (message.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) message.getContent();
			// Iteration through all the parts in order to find the one
			// containing the text
			for (int i = 0; i < mp.getCount(); i++) {
				s = getTextFromMessage(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

	/**
	 *Save the temporary file who contains the mail inputstream in /tmp
	 * 
	 * @param filename
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private File saveFile(String filename, InputStream inputStream)
			throws IOException {
		File f = new File("/tmp/" + filename);
		try {
			// Temporary until we find a better way to do it
			Files.copy(inputStream, f.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			LOGGER.error("Cannot create temporary file", e);
		}
		return f;
	}
 
	/**
	 * Upload the file into the user Media@Home account
	 * 
	 * @param is
	 * @param filename
	 * @param Type
	 * @return (String) link to see the document
	 * @throws IOException
	 */
	public String postFile(InputStream is, String filename, String Type)
			throws IOException {
		try {
			HttpAuthenticationFeature feature = HttpAuthenticationFeature
					.basic(this.username, this.password);

			// Configuration of the client to allow a post with a large file
			ClientConfig cc = new ClientConfig();
			cc.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
			cc.property(ClientProperties.CHUNKED_ENCODING_SIZE,
					Integer.valueOf(128));
			cc.property(ClientProperties.OUTBOUND_CONTENT_LENGTH_BUFFER,
					Integer.valueOf(128));
			Client client = ClientBuilder.newClient(cc);
			client.register(feature).register(MultiPartFeature.class);

			WebTarget target = client.target("http://"+CliConfSingleton.mediahome_host+":"+CliConfSingleton.mediahome_port+"/api/app/"
					+ this.username + "/content");

			LOGGER.info("Filename : " + filename);
			Response response = target
					.request()
					.header("Content-Disposition",
							"attachment; filename=" + filename)
					.post(Entity.entity(is, Type), Response.class);

			if (response.getLocation() != null)
				return "http://"
						+ localAddress()
						+ "/snapmail/"
						+ "snapmail.html#/"
						+ this.username
						+ "/"
						+ response.getLocation().toString().split("/content/")[1];

			else {
				LOGGER.error("Error during the upload : Media@Home did not return a location");
				return "Error during the upload";
			}

		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 403) {
				LOGGER.error("Error 403 (post content)");
			} else {
				throw e;
			}
		}
		return "Error";
	}

	/**
	 * Get the local network name of the server
	 * 
	 * @return String nameoftheserver
	 */
	@SuppressWarnings("finally")
	private String localAddress() {
		String add = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();

			add = addr.getHostName();
		} catch (UnknownHostException e) {
			System.exit(1);
		} finally {
			return add;
		}
	}
 
	/**
	 * Get the smtp properties of the user from Media@Home
	 * 
	 * @param properties
	 * @return Properties 
	 */
	private Properties setSMTPProperties(Properties properties) {

		try {
			HttpAuthenticationFeature feature = HttpAuthenticationFeature
					.basic(this.username, this.password);
			Client client = ClientBuilder.newClient();
			client.register(feature);

			WebTarget target = client
					.target("http://"+ CliConfSingleton.mediahome_host + ":" + CliConfSingleton.mediahome_port +"/api/app/snapmail/" + this.username + "/smtp");
			SmtpProperty smtpProperty = target.request(
					MediaType.APPLICATION_XML_TYPE).get(SmtpProperty.class);
			String token;
			// If token exist
			if (smtpProperty.getToken() == null)
				token = "";
			else
				token = smtpProperty.getToken();

			// If token is empty
			if (token.equals("")) {
				properties.setProperty("mail.smtp.auth", "true");
				properties.setProperty("mail.smtp.starttls.enable", "true"); // TLS
																				// Connection
				properties
						.setProperty("mail.smtp.host", smtpProperty.getHost()); // Remote
																				// SMTP
																				// server
																				// address
				properties.setProperty("mail.user", smtpProperty.getUsername()); // Username
																					// used
																					// to
																					// log
																					// into
																					// the
																					// remote
																					// SMTP
																					// server
				properties.setProperty("mail.password",
						smtpProperty.getPassword()); // Password used to log
														// into the remote SMTP
														// server
				properties
						.setProperty("mail.smtp.port", smtpProperty.getPort());
				properties.setProperty("mail.token", "");
			} else {
				properties.setProperty("mail.token", token);
			}
			LOGGER.info("Connection to the remote SMTP server ---> "
					+ smtpProperty.getUsername() + ":"
					+ smtpProperty.getPassword() + "@" + smtpProperty.getHost()
					+ ":" + smtpProperty.getPort());
			return properties;

		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 403) {
				// TODO
				// PAS DE COMPTE ou MAUVAIS ADRESSE/MDP
				LOGGER.error("Error 403 (get smtp property)");
			} else {
				throw e;
				
			}
		}
		return properties;
	}
 
	/**
	 * Method to get the token thanks to our json secret. The user need to agree and copy-paste a link into the terminal.
	 * 
	 * @param token
	 * @return Gmail service
	 * @throws IOException
	 */
	private Gmail getService(String token) throws IOException {

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
	public static GoogleCredential createCredentialWithRefreshToken(HttpTransport transport,
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
	 *  Query to yahoo to get a new access_token thanks to the refresh_token
	 *  
	 * @param token
	 * @return String yahooToken
	 */
	private String getYahooToken(String token) {
		final String Yahooclient_ID = CliConfSingleton.yahoo_clientID;
		final String Yahooclient_secret = CliConfSingleton.yahoo_clientsecret;
		final String redirectUri = CliConfSingleton.centralURL.toString() + "/api/oauth";
		
		Client client = ClientBuilder.newClient();
		String response;
		String data;
		WebTarget targetYahoo = client.target("https://api.login.yahoo.com/oauth2/get_token");
		
		String secure = Yahooclient_ID + ":" + Yahooclient_secret;
		String encodedvalue= java.util.Base64.getEncoder().encodeToString(secure.getBytes());

		data = "client_id=" + Yahooclient_ID
				+ "&client_secret=" + Yahooclient_secret
				+ "&refresh_token=" + token
				+ "&redirect_uri=" + redirectUri.replace(":9999","").replace(":8080", "")
				+ "&grant_type=refresh_token";

		response = targetYahoo
				.request()
				.header("Authorization", "Basic " + encodedvalue)
				.post(Entity.entity(data, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		LOGGER.info(response.toString());
		
		// Analysis of the response, get the access_token
		JSONObject json;
		String yahooToken="";
		
		try {
			json= new JSONObject(response);
			yahooToken=json.get("access_token").toString();
			LOGGER.info("Good Token");
		} catch (JSONException e) {
			LOGGER.error("Error with the token");
		}
		
		return yahooToken;
	}

	/**
	 *  Query to outlook to get a new access_token thanks to the refresh_token
	 *  
	 * @param token
	 * @return String outlookToken
	 */
	private String getOutlookToken(String token) {
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
private void sendOutlookMail(String token, MimeMessage message){
	String secure = "user={" + this.username + "}\1auth=Bearer{" + token + "}\1\1" ;
	String encodedvalue= java.util.Base64.getEncoder().encodeToString(secure.getBytes());
	Client client = ClientBuilder.newClient();
	
	
}

}
