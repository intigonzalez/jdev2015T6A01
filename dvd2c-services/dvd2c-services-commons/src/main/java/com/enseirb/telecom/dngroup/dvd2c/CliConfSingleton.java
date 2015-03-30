package com.enseirb.telecom.dngroup.dvd2c;

import java.io.IOException;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;

/**
 * a singleton that contains input from CLI
 * 
 * 
 *
 */
public class CliConfSingleton {
	public static String boxID;
	public static Integer port ;
	public static String ip ;
	public static String centralURL ;
	public static String contentPath ;
	public static String publicAddr;
	public static String dbHostname ;
	public static Integer dbPort;
	public static String rabbitHostname ;
	public static Integer rabbitPort;

	public static void defaultValue() {
		boxID = "BOX_TEST";
		port = 9998;
		ip = "0.0.0.0";
		centralURL = "http://central.homeb.tv:8080";
		contentPath = "/var/www/html";
		publicAddr = "http://localhost:9998";
		dbHostname = "localhost";
		dbPort = 27017;
		rabbitHostname = "localhost";
		rabbitPort = 5672;
	}

}
