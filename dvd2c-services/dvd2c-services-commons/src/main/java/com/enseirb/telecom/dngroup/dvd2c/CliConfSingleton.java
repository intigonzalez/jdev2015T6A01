package com.enseirb.telecom.dngroup.dvd2c;

/**
 * a singleton that contains input from CLI
 * 
 * 
 *
 */
public class CliConfSingleton {
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
	}

}
