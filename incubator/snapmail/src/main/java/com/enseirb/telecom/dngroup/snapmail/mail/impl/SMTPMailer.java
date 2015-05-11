package com.enseirb.telecom.dngroup.snapmail.mail.impl;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.enseirb.telecom.dngroup.snapmail.mail.Mailer;

public class SMTPMailer implements Mailer{
	
	private SMTPProperties smtpProperties;
	
	public void send(MimeMessage message){
		String host = smtpProperties.getHost();
		String port = smtpProperties.getPort();
		String username = smtpProperties.getUsername();
		String password = smtpProperties.getPassword();
		
		Properties properties = System.getProperties();
		
		// These properties will change based on the remote smtp server used

		// Get the default Session object.	
		properties.setProperty("mail.smtp.auth", "true");
		// TLS Connection
		properties.setProperty("mail.smtp.starttls.enable", "true");
		// Remote SMTP server address
		properties.setProperty("mail.smtp.host", host); 
		// Username used to log into the remote SMTP server
		properties.setProperty("mail.user", username);
		 // Password used to log into the remote SMTP server
		properties.setProperty("mail.password",password);
		properties.setProperty("mail.smtp.port", port);
		Session session =null;
		session = Session.getInstance(properties, null);
		session.setDebug(true);
		Transport tr = null;
		try {
			tr = session.getTransport("smtp");
			tr.connect(host, Integer.parseInt(port), username, password);
			tr.sendMessage(message, message.getAllRecipients());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public SMTPMailer(SMTPProperties smtpProperties) {
		super();
		this.smtpProperties = smtpProperties;
	}
}
