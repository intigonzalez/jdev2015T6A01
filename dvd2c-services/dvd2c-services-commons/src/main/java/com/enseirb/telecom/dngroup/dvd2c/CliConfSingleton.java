package com.enseirb.telecom.dngroup.dvd2c;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.InvalidOptionSpecificationException;

/**
 * a singleton that contains input from CLI
 * 
 * 
 *
 */
public class CliConfSingleton {
	private static final Logger LOGGER = LoggerFactory.getLogger(CliConfSingleton.class);
	public static String boxID;
	public static Integer port;
	public static String ip;
	public static String centralURL;
	public static String contentPath;
	public static String publicAddr;
	public static String dbHostname;
	public static Integer dbPort;
	public static String rabbitHostname;
	public static Integer rabbitPort;
	public static String google_clientID;
	public static String google_clientsecret;
	public static String yahoo_clientID;
	public static String yahoo_clientsecret;

	public static void defaultValue() {
		if (boxID == null)
			boxID = "BOX_TEST";
		if (port == null)
			port = 9998;
		if (ip == null)
			ip = "0.0.0.0";
		if (centralURL == null)
			centralURL = "http://central.homeb.tv:8080";
		if (contentPath == null)
			contentPath = "/var/www/html";
		if (publicAddr == null)
			publicAddr = "http://localhost:9998";
		if (dbHostname == null)
			dbHostname = "localhost";
		if (dbPort == null)
			dbPort = 27017;
		if (rabbitHostname == null)
			rabbitHostname = "localhost";
		if (rabbitPort == null)
			rabbitPort = 5672;
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
