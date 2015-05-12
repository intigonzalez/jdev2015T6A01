package com.enseirb.telecom.dngroup.snapmail;

import org.subethamail.smtp.server.SMTPServer;
import org.subethamail.smtp.auth.*;
import org.subethamail.smtp.helper.*;

import com.enseirb.telecom.dngroup.snapmail.Main.CliConf;
import com.enseirb.telecom.dngroup.snapmail.ApplicationContext;
import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.InvalidOptionSpecificationException;
import com.lexicalscope.jewel.cli.Option;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.core.Application;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

// Default CliConfSingleton
class CliConfSingleton {
	public static CliConf conf;
	public static String centralURL;
	public static String google_clientID;
	public static String google_clientsecret;
	public static String yahoo_clientID;
	public static String yahoo_clientsecret;
	public static String publicAddr;
	public static String mediahome_host;
	public static String mediahome_port;
	public static String clamav_host;
	public static String clamav_port;
	
	// Default value if there is no file and no arguments
	public static void defaultValue() {
		if(centralURL==null)
			centralURL = "http://central.homeb.tv:8080";
		if(mediahome_host==null)
			mediahome_host = "localhost";
		if(mediahome_port==null)
			mediahome_port = "9998";
		if(clamav_host==null)
			clamav_host = "127.0.0.1";
		if(clamav_port==null)
			clamav_port = "3310";
		if(google_clientID==null)
			google_clientID = "547107646254-3rhmcq9g7ip63rl9trr6ono0cn1t8ab6.apps.googleusercontent.com";
		if(google_clientsecret==null)
			google_clientsecret = "9lfX5WtjkWYiV2LrgTdhG62S";
		if(yahoo_clientID==null)
			yahoo_clientID = "dj0yJmk9Um5NdERwR1FSYVN1JmQ9WVdrOWQwSlJkMk5oTkRJbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1mMQ--";
		if(yahoo_clientsecret==null)
			yahoo_clientsecret = "26b146829545f8df236e2f4c44bc0cd168162d5e";
	}
}

public class Main {
//Cliconf for hard coded values
	interface CliConf {

		@Option(shortName="h", longName = "mediahome_host", defaultToNull=true)
		public String getMediaHomeHost();

		@Option(shortName="p", longName = "mediahome_port", defaultToNull=true)
		public String getMediaHomePort();

		@Option(shortName="c", longName = "clamav_host", defaultToNull=true)
		public String getClamAVHost();
		
		@Option(shortName="v", longName = "clamav_port", defaultToNull=true)
		public String getClamAVPort();
		
		@Option(longName = "publicAddr", description = "Public IP address", defaultToNull=true)
		String getPublicAddr();

		@Option(longName = "centralURL", description = "URL of the central server", defaultToNull=true)
		String getCentralUrl();
		
		@Option(longName = "google_clientID", description = "google clientID for Oauth2")
		String getGoogleClientID();
		
		@Option(longName = "google_clientsecret", description = "google client secret for Oauth2")
		String getGoogleClientSecret();
		
		@Option(longName = "yahoo_clientID", description = "yahoo clientID for Oauth2")
		String getYahooClientID();
		
		@Option(longName = "yahoo_clientsecret", description = "yahoo client secret for Oauth2")
		String getYahooClientSecret();

	}

	public static void main(String[] args) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableKeyException, KeyManagementException {

		//CliConfSingleton.conf = CliFactory.parseArguments(CliConf.class, args);
		getParametreFromArgs(args);

		// TLS
		// Creating our own SSLContext
		// Create and initialize the SSLContext with key material
		char[] passphrase = "secretsnapmail".toCharArray();

		// First initialize the key and trust material
		KeyStore ksKeys = KeyStore.getInstance("PKCS12");
		ksKeys.load(new FileInputStream("TLS/keystore_snapmail.jks"),
				passphrase);
		// Trust store contains certificates of trusted certificate authorities
		KeyStore ksTrust = KeyStore.getInstance("JKS");
		ksTrust.load(new FileInputStream("TLS/truststore_snapmail.ts"),
				passphrase);

		// KeyManagers decide which key material to use
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ksKeys, passphrase);

		// TrustManagers decide whether to allow connections
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(ksTrust);

		final SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		SimpleMessageListenerImpl mylistener = new SimpleMessageListenerImpl();
		SMTPServer smtpServer = new com.enseirb.telecom.dngroup.snapmail.SmtpServer(
				new SimpleMessageListenerAdapter(mylistener), sslContext);

		// smtpServer.setRequireTLS(true);
		smtpServer.setEnableTLS(true);// allow secure/insecure Connections, the
										// client choose.
		smtpServer
				.setAuthenticationHandlerFactory(new EasyAuthenticationHandlerFactory(
						mylistener));
		smtpServer.setPort(25004);
		smtpServer.start();
	}
	
	//Search configs in params
	static void getParametreFromArgs(String[] args) {
		try {
			CliConf cliconf = CliFactory.parseArguments(
					CliConf.class, args);

			CliConfSingleton.mediahome_host = cliconf.getMediaHomeHost();
		} catch (ArgumentValidationException e1) {
			

		} catch (InvalidOptionSpecificationException e1) {
		
		}
		try {
			CliConf cliconf = CliFactory.parseArguments(
					CliConf.class, args);
		} catch (ArgumentValidationException e1) {
			

		} catch (InvalidOptionSpecificationException e1) {
		
		}
		getParametreFromFile();
	}
// Search configs in a file	
static void getParametreFromFile() {
	String aPPath = "/etc/mediahome/box.properties";
	try {
		FileInputStream in = new FileInputStream(aPPath);
		ApplicationContext.properties.load(in);
		if (CliConfSingleton.publicAddr == null)
			CliConfSingleton.publicAddr = ApplicationContext.getProperties()
					.getProperty("publicAddr");
		if (CliConfSingleton.centralURL == null)
			CliConfSingleton.centralURL = ApplicationContext.getProperties()
					.getProperty("centralURL");
		if (CliConfSingleton.google_clientID == null)
			CliConfSingleton.google_clientID = ApplicationContext.getProperties()
					.getProperty("google_clientID");
		if (CliConfSingleton.google_clientsecret == null)
			CliConfSingleton.google_clientsecret = ApplicationContext.getProperties()
					.getProperty("google_clientsecret");
		if (CliConfSingleton.yahoo_clientID == null)
			CliConfSingleton.yahoo_clientID = ApplicationContext.getProperties()
					.getProperty("yahoo_clientID");
		if (CliConfSingleton.yahoo_clientsecret == null)
			CliConfSingleton.yahoo_clientsecret = ApplicationContext.getProperties()
					.getProperty("yahoo_clientsecret");
		if (CliConfSingleton.clamav_host == null)
			CliConfSingleton.clamav_host = ApplicationContext.getProperties()
					.getProperty("clamavHost");
		in.close();
		CliConfSingleton.defaultValue();
	} catch (FileNotFoundException e1) {
		CliConfSingleton.defaultValue();
	} catch (Exception e1) {
		CliConfSingleton.defaultValue();
	}
}
}

