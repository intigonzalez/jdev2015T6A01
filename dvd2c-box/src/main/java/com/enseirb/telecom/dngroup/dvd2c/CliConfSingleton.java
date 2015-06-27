package com.enseirb.telecom.dngroup.dvd2c;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a singleton that contains input from CLI
 * 
 * 
 *
 */
public class CliConfSingleton {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CliConfSingleton.class);
	public static String boxID;
	public static Integer appPort;
	public static String appHostName;
	public static String centralURL;
	public static String contentPath;
	public static String publicAddr;

	public static String rabbitHostname;
	public static Integer rabbitPort;
	public static String snapmail_host;
	public static String database_password;
	public static String database_url;
	public static String database_username;

	public static void defaultValue() {
		if (boxID == null)
			boxID = "BOX_TEST";
		if (appPort == null)
			appPort = 9998;
		if (appHostName == null)
			appHostName = "0.0.0.0";
		if (centralURL == null)
			centralURL = "http://central.homeb.tv:8080";
		if (contentPath == null)
			contentPath = "/var/www/html";
		if (publicAddr == null)
			publicAddr = "http://localhost:9998";
		// if (dbHostname == null)
		// dbHostname = "localhost";
		// if (dbPort == null)
		// dbPort = 27017;
		if (rabbitHostname == null)
			rabbitHostname = "localhost";
		if (rabbitPort == null)
			rabbitPort = 5672;
		if (snapmail_host == null)
			snapmail_host = "http://localhost:9997";
		if (database_password == null)
			database_password = "mediahome";
		if (database_url == null)
			database_url = "jdbc:mysql://localhost:3306/mediahome";
		if (database_username == null)
			database_username = "mediahome";

	}

	public static URI getBaseURI() {

		return UriBuilder.fromUri("http://" + appHostName + ":" + appPort)
				.build();
	}

	public static URI getBaseApiURI() {

		return UriBuilder.fromUri(getBaseURI()).path("api").build();
	}

}
