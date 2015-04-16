package com.enseirb.telecom.dngroup.snapmail;

import org.subethamail.smtp.server.SMTPServer;
import org.subethamail.smtp.auth.*;
import org.subethamail.smtp.helper.*;

import com.enseirb.telecom.dngroup.snapmail.Main.CliConf;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

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

class CliConfSingleton {
	public static CliConf conf;
}

public class Main {

	interface CliConf {

		@Option(shortName = "h", defaultValue = "localhost")
		public String getMediaHomeHost();

		@Option(shortName = "p", defaultValue = "9998")
		public String getMediaHomePort();

		@Option(shortName = "p_cav", defaultValue = "3310")
		public String getClamAVPort();

	}

	public static void main(String[] args) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableKeyException, KeyManagementException {

		CliConfSingleton.conf = CliFactory.parseArguments(CliConf.class, args);

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
}
