package com.enseirb.telecom.dngroup.dvd2c.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a singleton that contains input from CLI
 * 
 * 
 *
 */
public class CliConfSingletonC {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CliConfSingletonC.class);
	public static Integer appPort;
	public static String appHostName;
	public static String dbHostname;
	public static Integer dbPort;

	public static void defaultValue() {

		if (appPort == null)
			appPort = 9998;
		if (appHostName == null)
			appHostName = "0.0.0.0";
		if (dbHostname == null)
			dbHostname = "localhost";
		if (dbPort == null)
			dbPort = 27017;

	}
}
