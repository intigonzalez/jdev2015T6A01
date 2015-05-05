package com.enseirb.telecom.dngroup.snapmail.cli;

import com.lexicalscope.jewel.cli.Option;

//Cliconf for hard coded values
public interface CliConf {

	@Option(shortName="h", longName = "mediahome_host", defaultValue = "localhost")
	public String getMediaHomeHost();

	@Option(shortName="p", longName = "mediahome_port", defaultValue = "9998")
	public String getMediaHomePort();

	@Option(shortName="c", longName = "clamav_host", defaultValue = "127.0.0.1")
	public String getClamAVHost();
	
	@Option(shortName="v", longName = "clamav_port", defaultValue = "3310")
	public String getClamAVPort();
	
	@Option(longName = "centralURL", description = "URL of the central server")
	String getCentralUrl();
	
	@Option(longName = "google_clientID", description = "google clientID for Oauth2")
	String getGoogleClientID();
	
	@Option(longName = "google_clientsecret", description = "google client secret for Oauth2")
	String getGoogleClientSecret();
	
	@Option(longName = "yahoo_clientID", description = "yahoo clientID for Oauth2")
	String getYahooClientID();
	
	@Option(longName = "yahoo_clientsecret", description = "yahoo client secret for Oauth2")
	String getYahooClientSecret();

}