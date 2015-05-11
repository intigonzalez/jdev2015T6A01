package com.enseirb.telecom.dngroup.snapmail;

import org.subethamail.smtp.server.SMTPServer;
import org.subethamail.smtp.auth.*;
import org.subethamail.smtp.helper.*;

import com.enseirb.telecom.dngroup.snapmail.cli.ApplicationContext;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConf;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;
import com.enseirb.telecom.dngroup.snapmail.mail.SimpleMessageListenerImpl;
import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.InvalidOptionSpecificationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;



public class Main {
	public static void main(String[] args) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableKeyException, KeyManagementException, InterruptedException {

		// CliConfSingleton.conf = CliFactory.parseArguments(CliConf.class,
		// args);
		getParametreFromArgs(args);

		//Start the grizzly server in a thread
		ThreadRestServer webServer = new ThreadRestServer();
		webServer.start();
		
		
		// TLS
		// Creating our own SSLContext
		// Create and initialize the SSLContext with key material
		char[] passphrase = "secretsnapmail".toCharArray();

		// First initialize the key and trust material
		KeyStore ksKeys = KeyStore.getInstance("PKCS12");
		//NH: don't we need to close the fileinputstream?
		ksKeys.load(new FileInputStream("TLS/keystore_snapmail.jks"),
				passphrase);
		
		// Trust store contains certificates of trusted certificate authorities
		KeyStore ksTrust = KeyStore.getInstance("JKS");
		//NH: don't we need to close the fileinputstream?
		ksTrust.load(new FileInputStream("TLS/truststore_snapmail.ts"),
				passphrase);

		// KeyManagers decide which key material to use
		final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ksKeys, passphrase);

		// TrustManagers decide whether to allow connections
		final TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(ksTrust);

		final SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		final SimpleMessageListenerImpl mylistener = new SimpleMessageListenerImpl();
		final SMTPServer smtpServer = new com.enseirb.telecom.dngroup.snapmail.mail.SnapMailSMTPServer(
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

	// Search configs in params
	static void getParametreFromArgs(String[] args) {
		try {
			CliConf cliconf = CliFactory.parseArguments(CliConf.class, args);

			CliConfSingleton.mediahome_host = cliconf.getMediaHomeHost();
		} catch (ArgumentValidationException e1) {

		} catch (InvalidOptionSpecificationException e1) {

		}
		try {
			CliConf cliconf = CliFactory.parseArguments(CliConf.class, args);
			CliConfSingleton.clamav_host = cliconf.getMediaHomeHost();
		} catch (ArgumentValidationException e1) {

		} catch (InvalidOptionSpecificationException e1) {

		}
		initCliConfSingleton();
	}

	// Search configs in a file
	static void initCliConfSingleton() {
		String aPPath = "/etc/mediahome/box.properties";
		try {
			FileInputStream in = new FileInputStream(aPPath);
			ApplicationContext.properties.load(in);
			if (CliConfSingleton.mediahome_host == null)
			CliConfSingleton.mediahome_host = ApplicationContext.getProperties()
					.getProperty("publicAddr");
			if (CliConfSingleton.centralURL == null)
				CliConfSingleton.centralURL = ApplicationContext
						.getProperties().getProperty("centralURL");
			if (CliConfSingleton.google_clientID == null)
				CliConfSingleton.google_clientID = ApplicationContext
						.getProperties().getProperty("google_clientID");
			if (CliConfSingleton.google_clientsecret == null)
				CliConfSingleton.google_clientsecret = ApplicationContext
						.getProperties().getProperty("google_clientsecret");
			if (CliConfSingleton.yahoo_clientID == null)
				CliConfSingleton.yahoo_clientID = ApplicationContext
						.getProperties().getProperty("yahoo_clientID");
			if (CliConfSingleton.yahoo_clientsecret == null)
				CliConfSingleton.yahoo_clientsecret = ApplicationContext
						.getProperties().getProperty("yahoo_clientsecret");
			in.close();
			CliConfSingleton.defaultValue();
		} catch (FileNotFoundException e1) {
			CliConfSingleton.defaultValue();
		} catch (Exception e1) {
			CliConfSingleton.defaultValue();
		}
	}
}
