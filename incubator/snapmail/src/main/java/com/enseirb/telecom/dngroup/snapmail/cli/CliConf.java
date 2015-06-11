package com.enseirb.telecom.dngroup.snapmail.cli;

import com.lexicalscope.jewel.cli.Option;

//Cliconf for hard coded values
public interface CliConf {

	@Option(shortName="h", longName = "mediahome_host", defaultToNull=true)
	public String getMediaHomeHost();

	@Option(shortName="p", longName = "mediahome_port", defaultToNull=true)
	public String getMediaHomePort();

	@Option(shortName="c", longName = "clamav_host", defaultToNull=true)
	public String getClamAVHost();
	
	@Option(shortName="v", longName = "clamav_port", defaultToNull=true)
	public String getClamAVPort();
	
	@Option(longName = "publicAddr", description = "Public IP address", defaultToNull=true)
	String getPublicAddr();

	@Option(longName = "centralURL", description = "URL of the central server", defaultToNull=true)
	String getCentralUrl();
	
	@Option(longName = "snapmail_host", description = "URL of Snapmail_Host", defaultToNull=true)
	String getSnapmail_Host();
	
	@Option(longName = "google_clientID", description = "google clientID for Oauth2", defaultToNull=true)
	String getGoogleClientID();
	
	@Option(longName = "google_clientsecret", description = "google client secret for Oauth2", defaultToNull=true)
	String getGoogleClientSecret();
	
	@Option(longName = "yahoo_clientID", description = "yahoo clientID for Oauth2", defaultToNull=true)
	String getYahooClientID();
	
	@Option(longName = "yahoo_clientsecret", description = "yahoo client secret for Oauth2", defaultToNull=true)
	String getYahooClientSecret();

}