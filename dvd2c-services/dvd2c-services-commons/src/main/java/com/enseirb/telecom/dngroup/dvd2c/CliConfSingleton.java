package com.enseirb.telecom.dngroup.dvd2c;

/**
 * a singleton that contains input from CLI
 * 
 * 
 *
 */
public class CliConfSingleton {
	public static String boxID = "BOX_TEST";
	public static Integer port = 9998;
	public static String ip = "0.0.0.0";
	public static String centralURL = "http://central.homeb.tv:8080";
	public static String contentPath = "/var/www/html";
	public static String publicAddr = "http://localhost:9998";
	public static String dbHostname = "localhost";
	public static Integer dbPort = 27017;
	public static String rabbitHostname = "localhost";
	public static Integer rabbitPort = 5672;

}
