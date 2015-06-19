package com.enseirb.telecom.dngroup.snapmail.cli;

import com.enseirb.telecom.dngroup.snapmail.cli.CliConf;
import com.enseirb.telecom.dngroup.snapmail.server.conf.ServerName;


// Default CliConfSingleton
public class CliConfSingleton {
	public static CliConf conf;
	public static String centralURL;
	public static String google_clientID;
	public static String google_clientsecret;
	public static String yahoo_clientID;
	public static String yahoo_clientsecret;
	public static String publicAddr;
	public static String mediahome_host;
	public static String mediahome_port;
	public static String clamav_host;
	public static String clamav_port;
	public static String snapmail_host;
	
	// Default value if there is no file and no arguments
	public static void defaultValue() {
		if(centralURL==null)
			centralURL = "http://central.homeb.tv:8080";
		if(mediahome_host==null)
			mediahome_host = "http://localhost:9998";
		if(publicAddr==null)
			publicAddr = "0.0.0.0";
		if(mediahome_port==null)
			mediahome_port = "9998";
		if(clamav_host==null)
			clamav_host = "127.0.0.1";
		if(clamav_port==null)
			clamav_port = "3310";
		if(snapmail_host==null)
			snapmail_host="http://"+ ServerName.localAddress() + ":9997";
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