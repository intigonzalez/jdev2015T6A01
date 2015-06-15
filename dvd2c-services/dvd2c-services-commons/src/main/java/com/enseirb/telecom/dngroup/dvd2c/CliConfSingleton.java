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
	public static Integer appPort;
	public static String appHostName;
	public static String centralURL;
	public static String contentPath;
	public static String publicAddr;
	public static String dbHostname;
	public static Integer dbPort;
	public static String rabbitHostname;
	public static Integer rabbitPort;


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
		if (dbHostname == null)
			dbHostname = "localhost";
		if (dbPort == null)
			dbPort = 27017;
		if (rabbitHostname == null)
			rabbitHostname = "localhost";
		if (rabbitPort == null)
			rabbitPort = 5672;
	}
}
