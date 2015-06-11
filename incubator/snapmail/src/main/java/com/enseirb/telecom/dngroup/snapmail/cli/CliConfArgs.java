package com.enseirb.telecom.dngroup.snapmail.cli;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.InvalidOptionSpecificationException;

public class CliConfArgs {
	// Search configs in params
	public static void getParametreFromArgs(String[] args) {
		try {
			CliConf cliconf = CliFactory.parseArguments(CliConf.class, args);

			CliConfSingleton.mediahome_host = cliconf.getMediaHomeHost();
			getParametreFromFile();
		} catch (ArgumentValidationException e1) {

		} catch (InvalidOptionSpecificationException e1) {

		}
		try {
			CliConf cliconf = CliFactory.parseArguments(CliConf.class, args);
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
			if (CliConfSingleton.mediahome_host == null)
				CliConfSingleton.mediahome_host = ApplicationContext
						.getProperties().getProperty("publicAddr");
			if (CliConfSingleton.centralURL == null)
				CliConfSingleton.centralURL = ApplicationContext
						.getProperties().getProperty("centralURL");
			if (CliConfSingleton.snapmail_host == null)
				CliConfSingleton.snapmail_host = ApplicationContext
						.getProperties().getProperty("snapmail_host");
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
			if (CliConfSingleton.clamav_host == null)
				CliConfSingleton.clamav_host = ApplicationContext
						.getProperties().getProperty("clamavHost");
			in.close();
			CliConfSingleton.defaultValue();
		} catch (FileNotFoundException e1) {
			CliConfSingleton.defaultValue();
		} catch (Exception e1) {
			CliConfSingleton.defaultValue();
		}
	}
}
