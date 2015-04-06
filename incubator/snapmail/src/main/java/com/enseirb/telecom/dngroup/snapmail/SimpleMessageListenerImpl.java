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

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.auth.UsernamePasswordValidator;
import org.subethamail.smtp.helper.*;

import java.io.File;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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

public class SimpleMessageListenerImpl implements SimpleMessageListener, UsernamePasswordValidator{
	private final static Logger LOGGER = LoggerFactory.getLogger(SimpleMessageListener.class);
 
	protected String username;
	protected String password;
	
	//to display all recipients
	ArrayList<String> recipientArray = new ArrayList<String>();
	private String allrecipients;
	private int counter = 0;
	
	//to certify that recipients are real adresses
	private int position = 0;
	
	MessageContext context;
	private String subject;
	private String text;
	private String type;
	private InputStream data;

	//private final static Logger LOGGER = Logger.getLogger(SimpleMessageListenerImpl.class.getName());
	
	@Override
    public void login(String username, String password) {
		this.username = username;
		this.password = password;
    }
	
	@Override
	public boolean accept(String from, String recipient){
		position = recipient.indexOf("@");
		if (position != -1){//If the address is correct mail address
			recipientArray.add(""+recipient);
			counter++;
		}
		return true;
	}
	
	@Override
	public void deliver(String from, String recipient, InputStream data) throws TooMuchDataException, IOException{
		//Emptying the array
		counter--;
		if (counter == 0){
			allrecipients = recipientArray.toString();
			this.allrecipients = this.allrecipients.substring(1, this.allrecipients.length()-1);
		    LOGGER.info("\n--------------------------------------------------");
		    LOGGER.info("FROM: "+from+"\n TO: "+allrecipients);
			//data
			this.data = data;
			Properties props = new Properties();
			Session session = Session.getInstance(props, null);
	
			// Creation of a new MIME Message
			MimeMessage newMessage;
			Multipart multiPart = new MimeMultipart();//mixed, related
			
			try {
				// The mail received as a stream of data by the app is stored into the MIME message
				File mimeFile=saveFile("snapmail", data);
		        SharedFileInputStream fis = new SharedFileInputStream(mimeFile);
				newMessage = new MimeMessage(session, fis);
		        mimeFile.delete();
		        
				this.subject = newMessage.getSubject();
				String contentType = newMessage.getContentType();
				this.type = contentType;
				Boolean isMultipart = contentType.contains("multipart");
				
				// Returns the text of the message
				this.text = getTextFromMessage(newMessage);

				LOGGER.info("Text : \n\t"+this.text);
				
				// If the message contains an attachment it is parsed to upload the file on the cloud
				if(isMultipart) {
					LOGGER.info("This mail is multipart");
					parseMessage(newMessage,multiPart);
				}
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		sendMail("smtp.gmail.com", recipient, multiPart);
		recipientArray.clear();
		}
	}
	
	private void sendMail(String host, String recipient, Multipart multiPart)
	{
		// Get system properties
		 Properties properties = System.getProperties();
		 
			properties = setSMTPProperties(properties);
//			properties.setProperty("mail.smtp.auth", "true");
//			properties.setProperty("mail.smtp.starttls.enable", "true");	// TLS Connection
//			properties.setProperty("mail.smtp.host", host);					// Remote SMTP server address
//			properties.setProperty("mail.user", this.username);				// Username used to log into the remote SMTP server
//			properties.setProperty("mail.password", this.password);			// Password used to log into the remote SMTP server
//			properties.setProperty("mail.smtp.port", "587");
			final String pwd= properties.getProperty("mail.password");
			final String usr=properties.getProperty("mail.user");
		// These properties will change based on the remote smtp server used
			   
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties,
		new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
			// Remote SMTP credentials
			return new PasswordAuthentication(usr,pwd);
			}
		}
		);
		
		try {
			// Creation of the message that will be send in place of the received mail
			// The attachments are removed and replaced by links which redirect to the file stored in the cloud
			//
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
	        message.setFrom(new InternetAddress(username));
	        //message.setHeader("Content-Type", this.type);
	        LOGGER.info("CONTENT-TYPE : "+this.type);
	        // Set To: header field of the header.
	        message.addRecipients(Message.RecipientType.TO,allrecipients);
	        // Set Subject: header field
	        message.setSubject(this.subject);
	        // Now set the actual message
	        //message.setContent(this.text, this.type);
	        MimeBodyPart textPart = new MimeBodyPart();
	        textPart.setContent(this.text, this.type);
		    multiPart.addBodyPart(textPart);
	        message.setContent(multiPart);
	        LOGGER.info("Mail rebuilt and ready to be sent");
			Transport.send(message);
			LOGGER.info("Mail sent successfully !");
			LOGGER.info("--------------------------------------------------\n\n");
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
	private void parseMessage(Message message, Multipart multiPart) throws MessagingException, IOException {
		boolean attachment = false;
		// Since the message is multipart, it can be casted as such
		Multipart multipart = (Multipart)message.getContent();

		// Iteration through the different parts of the message
		for (int j = 0; j < multipart.getCount(); j++) {
			BodyPart bodyPart = multipart.getBodyPart(j);
			// A signed email will put the attachments into a MIME multipart,
			// thus if a part is a MIME multipart it is likely to contain attachments
			if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
				// If the part is not an attachment, but a MimeMultipart...
				if (bodyPart.getContent().getClass().equals(MimeMultipart.class)) {
					MimeMultipart mimemultipart = (MimeMultipart)bodyPart.getContent();
					// Iteration through the different parts of the MIME multipart
					for (int k=0;k<mimemultipart.getCount();k++) {
						if (mimemultipart.getBodyPart(k).getContentType().indexOf("text") == -1 && mimemultipart.getBodyPart(k).getContentType().indexOf("multipart") == -1)
						{
							if ( Part.INLINE != null | mimemultipart.getBodyPart(k).getHeader("Content-ID")[0] != null){//if attachment + embedded picture then the inline disposition is not writen
								LOGGER.info("embedded picture");
								
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
							}
							else if (Part.ATTACHMENT != null){
								processAttachment(mimemultipart.getBodyPart(k).getFileName(),mimemultipart.getBodyPart(k).getInputStream(),mimemultipart.getBodyPart(k).getContentType().substring(0, bodyPart.getContentType().indexOf(";")));
								attachment = true;
							}
						}
						//if there is another mimemultipart
						if (mimemultipart.getBodyPart(k).getContentType().indexOf("multipart") != -1){
							MimeMultipart mimemultipart_bis = (MimeMultipart) mimemultipart.getBodyPart(k).getContent();
							// Iteration through the different parts of the MIME multipart
							for (int l=0;l<mimemultipart_bis.getCount();l++) {
								if (mimemultipart_bis.getBodyPart(l).getContentType().indexOf("text") == -1)
								{
									if ( Part.INLINE != null | mimemultipart_bis.getBodyPart(l).getHeader("Content-ID")[0] != null){//if attachment + embedded picture then the inline disposition is not wrote
										LOGGER.info("embedded picture");
										
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
									}
									else if (Part.ATTACHMENT != null){
										processAttachment(mimemultipart_bis.getBodyPart(l).getFileName(),mimemultipart_bis.getBodyPart(l).getInputStream(),mimemultipart_bis.getBodyPart(l).getContentType().substring(0, bodyPart.getContentType().indexOf(";")));
										attachment = true;
									}
								}
							}
						}	
					}
				}
				continue;
			}
			// If the current part is explicitly an attachment...
			String contentType = bodyPart.getContentType().substring(0, bodyPart.getContentType().indexOf(";"));
			processAttachment(bodyPart.getFileName(), bodyPart.getInputStream(), contentType);
			attachment = true;
			LOGGER.info("Content type : "+ contentType);
		}
		
		if(attachment)
			this.text += "------------------------------------";
	}
	
	private void processAttachment(String filename, InputStream is, String Type) throws IOException {
		this.text += "Attachment : "+filename+"\n";
		String link = postFile(is, filename, Type);
		this.text += "Link :" + link + "\n";
	}
	
	private String getTextFromMessage(Part message) throws MessagingException, IOException {
		String s = new String();
		this.type = message.getContentType();
	
		// If the type is text/html or text/plain, the text is extracted directly
		if (message.isMimeType("text/*")) {
			s = (String)message.getContent();
			return s;
		}
			
		// Each part of a multipart/alternative type has the same content as the others,
		// but in a different format 
		if (message.isMimeType("multipart/alternative")) {
		   Multipart mp = (Multipart)message.getContent();
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
			   }
			   else if (bp.isMimeType("text/html")) {
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
			Multipart mp = (Multipart)message.getContent();
			// Iteration through all the parts in order to find the one containing the text
			for (int i = 0; i < mp.getCount(); i++) {
				s = getTextFromMessage(mp.getBodyPart(i));
				if (s != null)
					return s;
			}	
		}
	
		return null;
	}
	
	private File saveFile(String filename, InputStream inputStream) throws IOException {
		File f = new File("/tmp/" + filename);
		try {
			// Temporary until we find a better way to do it
			Files.copy(inputStream, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e) {
			LOGGER.error("Cannot create temporary file", e);
		}
		return f;
	}
	
	public String postFile(InputStream is, String filename, String Type) throws IOException
	{	
		try {
			HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(this.username, this.password);
			
			// Configuration of the client to allow a post with a large file
			ClientConfig cc = new ClientConfig();
			cc.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED"); 
			cc.property(ClientProperties.CHUNKED_ENCODING_SIZE, Integer.valueOf(128));
			cc.property(ClientProperties.OUTBOUND_CONTENT_LENGTH_BUFFER, Integer.valueOf(128));
		    Client client=ClientBuilder.newClient(cc);
		    client.register(feature).register(MultiPartFeature.class);
		
		    WebTarget target = client.target("http://localhost:9998/api/app/" + this.username + "/content");

		    LOGGER.info("Filename : "+filename);
		    Response response = target.request().header("Content-Disposition", "attachment; filename="+ filename).post(Entity.entity(is, Type), Response.class);

			if(response.getLocation() != null)
				return "http://" + localAddress() + "/" + "snapmail.html#/" + this.username + "/" + response.getLocation().toString().split("/content/")[1];
				
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
	
	@SuppressWarnings("finally")
	private String localAddress(){
		String add="";
		try{
		InetAddress addr = InetAddress.getLocalHost();
		
		add = addr.getHostName();
		} catch(UnknownHostException e){
			System.exit(1);
		}finally{
		return add;
		}
	}
	
	private Properties setSMTPProperties(Properties properties) {

		try {
			HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(this.username, this.password);			
			Client client = ClientBuilder.newClient();
			client.register(feature);

			WebTarget target = client.target("http://localhost/api/app/account/"+this.username+"/smtp");
			SmtpProperty smtpProperty = target.request(MediaType.APPLICATION_XML_TYPE).get(SmtpProperty.class);
			
			properties.setProperty("mail.smtp.auth", "true");
			properties.setProperty("mail.smtp.starttls.enable", "true");	// TLS Connection
			properties.setProperty("mail.smtp.host", smtpProperty.getHost());					// Remote SMTP server address
			properties.setProperty("mail.user", smtpProperty.getUsername());				// Username used to log into the remote SMTP server
			properties.setProperty("mail.password", smtpProperty.getPassword());			// Password used to log into the remote SMTP server
			properties.setProperty("mail.smtp.port", smtpProperty.getPort());
					
			LOGGER.info("Connection to the remote SMTP server ---> "+smtpProperty.getUsername()+":"+smtpProperty.getPassword()+"@"+smtpProperty.getHost()+":"+smtpProperty.getPort());
			return properties;
	        
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 403) {
				//TODO
				// PAS DE COMPTE ou MAUVAIS ADRESSE/MDP
				LOGGER.error("Error 403 (get smtp property)");
			} else {
				throw e;
			}
		}
		return properties;
	}

}
