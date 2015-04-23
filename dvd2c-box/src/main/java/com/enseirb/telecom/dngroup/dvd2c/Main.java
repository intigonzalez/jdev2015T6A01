package com.enseirb.telecom.dngroup.dvd2c;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jettison.JettisonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.filtering.SecurityEntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.endpoints.BoxEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.service.BoxService;
import com.enseirb.telecom.dngroup.dvd2c.service.BoxServiceImpl;
import com.google.common.base.Throwables;
import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.InvalidOptionSpecificationException;
import com.lexicalscope.jewel.cli.Option;
import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;


public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private static int getPort(int defaultPort) {
		// grab port from environment, otherwise fall back to default port 9998
		return CliConfSingleton.port;
	}

	private static URI getBaseURI() {
		String ip = CliConfSingleton.ip;
		return UriBuilder.fromUri("http://" + ip + "/api/")
				.port(getPort(CliConfSingleton.port)).build();
	}

	protected static HttpServer startServer() throws IOException {

		ResourceConfig resources = new ResourceConfig();
		resources.packages("com.enseirb.telecom.dngroup.dvd2c.endpoints");
		resources.register(CORSResponseFilter.class);
		resources.register(MultiPartFeature.class);
		resources.register(JettisonFeature.class);
		/**
		 * this two follow line is for security
		 */
		resources.register(SecurityEntityFilteringFeature.class);

		resources.register(SecurityRequestFilter.class);

		// System.out.println("Starting grizzly2...");
		LOGGER.info("Starting grizzly2");

		LOGGER.info("wadl here -> /api/application.wadl");

		// return GrizzlyServerFactory.createHttpServer(BASE_URI,
		// resourceConfig);
		LOGGER.info("Send information to the server central ...");
		try {
			BoxService boxManager = new BoxServiceImpl(new BoxRepositoryMongo(
					"mediahome"));
			boxManager.updateBox();

			LOGGER.info("Sucess ");
		} catch (ProcessingException e) {
			LOGGER.error(
					"Error for send information to the server central. Is running ?",
					e);
		} catch (Exception e) {
			LOGGER.error("Error for send information to the server central.", e);

		}

		return GrizzlyHttpServerFactory.createHttpServer(getBaseURI(),
				resources);
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// Properties

		getParametreFromArgs(args);

		LOGGER.info("the box ID is : {}", CliConfSingleton.boxID);

		// Grizzly 2 initialization
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					HttpServer httpServer = startServer();


					httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("/var/www/html/videos"), "/videos");
					httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("/var/www/html/pictures"), "/pictures");
					httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("/var/www/html/cloud"), "/cloud");

					httpServer.getServerConfiguration().addHttpHandler(
							new CLStaticHttpHandler(
									Main.class.getClassLoader(), "/"));

				} catch (IOException e) {
					throw Throwables.propagate(e);
				}
			}
		}).start();

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw e;
		}

		// httpServer.stop();
	}
	/**
	 * @param args
	 */
	static void getParametreFromArgs(String[] args) {
		try {
			CliConfiguration cliconf = CliFactory.parseArguments(
					CliConfiguration.class, args);

			CliConfSingleton.boxID = cliconf.getBoxID();
			CliConfSingleton.centralURL = cliconf.getCentralURL();
			CliConfSingleton.contentPath = cliconf.getContentPath();
			CliConfSingleton.ip = cliconf.getIp();
			CliConfSingleton.publicAddr = cliconf.getPublicAddr();
			CliConfSingleton.dbHostname = cliconf.getDbHostname();
			CliConfSingleton.dbPort = cliconf.getDbPort();
			CliConfSingleton.rabbitHostname = cliconf.getRabbitHost();
			CliConfSingleton.rabbitPort = cliconf.getRabbitPort();
			CliConfSingleton.port = cliconf.getPort();
			CliConfSingleton.google_clientID = cliconf.getGoogleClientID();
			CliConfSingleton.google_clientsecret = cliconf.getGoogleClientSecret();
			CliConfSingleton.yahoo_clientID = cliconf.getYahooClientID();
			CliConfSingleton.yahoo_clientsecret = cliconf.getYahooClientSecret();
			getParametreFromFile();
		} catch (ArgumentValidationException e1) {

			throw e1;
//			getParametreFromFile();

		} catch (InvalidOptionSpecificationException e1) {
			throw e1;
		}
	}

	/**
	 * 
	 */
	static void getParametreFromFile() {
		String aPPath = "/etc/mediahome/box.properties";
		try {
			FileInputStream in = new FileInputStream(aPPath);
			ApplicationContext.properties.load(in);
			if (CliConfSingleton.boxID == null)
				CliConfSingleton.boxID = ApplicationContext.getProperties()
						.getProperty("boxID");
			if (CliConfSingleton.centralURL == null)
				CliConfSingleton.centralURL = ApplicationContext
						.getProperties().getProperty("centralURL");
			if (CliConfSingleton.contentPath == null)
				CliConfSingleton.contentPath = ApplicationContext
						.getProperties().getProperty("contentPath");
			if (CliConfSingleton.ip == null)
				CliConfSingleton.ip = ApplicationContext.getProperties()
						.getProperty("ip");
			if (CliConfSingleton.publicAddr == null)
				CliConfSingleton.publicAddr = ApplicationContext
						.getProperties().getProperty("publicAddr");
			if (CliConfSingleton.dbHostname == null)
				CliConfSingleton.dbHostname = ApplicationContext
						.getProperties().getProperty("dbHostname");
			if (CliConfSingleton.dbPort == null)
				CliConfSingleton.dbPort = Integer.valueOf(ApplicationContext
						.getProperties().getProperty("dbPort"));
			if (CliConfSingleton.rabbitHostname == null)
				CliConfSingleton.rabbitHostname = ApplicationContext
						.getProperties().getProperty("rabbitHostname");
			if (CliConfSingleton.rabbitPort == null)
				CliConfSingleton.rabbitPort = Integer
						.valueOf(ApplicationContext.getProperties()
								.getProperty("rabbitPort"));
			if (CliConfSingleton.port == null)
				CliConfSingleton.port = Integer.valueOf(ApplicationContext
						.getProperties().getProperty("port"));
			if (CliConfSingleton.google_clientID == null)
				CliConfSingleton.google_clientID = ApplicationContext.getProperties()
						.getProperty("google_clientID");
			if (CliConfSingleton.google_clientsecret == null)
				CliConfSingleton.google_clientsecret = ApplicationContext.getProperties()
						.getProperty("google_clientsecret");
			if (CliConfSingleton.yahoo_clientID == null)
				CliConfSingleton.yahoo_clientID = ApplicationContext.getProperties()
						.getProperty("yahoo_clientID");
			if (CliConfSingleton.yahoo_clientsecret == null)
				CliConfSingleton.yahoo_clientsecret = ApplicationContext.getProperties()
						.getProperty("yahoo_clientsecret");
			LOGGER.info("File found use this values or arg Path ={} ",
					aPPath);
			CliConfSingleton.defaultValue();
			in.close();
		} catch (FileNotFoundException e1) {
			LOGGER.info("File not found use default value or arg Path ={} ",
					aPPath);
			CliConfSingleton.defaultValue();
		} catch (Exception e1) {
			LOGGER.info("File error not complete ", aPPath);
			CliConfSingleton.defaultValue();
		}
	}
}

interface CliConfiguration {

	@Option(shortName = "b", longName = "boxID" , defaultToNull = true)
	String getBoxID();

	@Option(shortName = "p", longName = "port", description = "the port on which the frontend will listen for http connections", defaultToNull = true)
	Integer getPort();

	@Option(shortName = "i", longName = "ip", description = "the IP on which the frontend will listen for http connections", defaultToNull = true)
	String getIp();

	@Option(longName = "content-path", description = "path of content", defaultToNull = true)
	String getContentPath();

	@Option(shortName = "c", longName = "central-addr", description = "the http addr of central server", defaultToNull = true)
	String getCentralURL();

	@Option(shortName = "a", longName = "public-addr", description = "the http addr of curent box", defaultToNull = true)
	String getPublicAddr();

	@Option(longName = "db-hostname", description = "the hostname of database", defaultToNull = true)
	String getDbHostname();

	@Option(longName = "db-port", description = "the port of database", defaultToNull = true)
	Integer getDbPort();

	@Option(longName = "rabbit-host", description = "the host of rabbitMQ", defaultToNull = true)
	String getRabbitHost();

	@Option(longName = "rabbit-port", description = "the port of rabbitMQ", defaultToNull = true)
	Integer getRabbitPort();
	
	@Option( longName = "google_clientID", description = "google clientID for Oauth2", defaultToNull = true)
	String getGoogleClientID();
	
	@Option( longName = "google_clientsecret", description = "google client secret for Oauth2", defaultToNull = true)
	String getGoogleClientSecret();
	
	@Option( longName = "yahoo_clientID", description = "yahoo clientID for Oauth2", defaultToNull = true)
	String getYahooClientID();
	
	@Option( longName = "yahoo_clientsecret", description = "yahoo client secret for Oauth2", defaultToNull = true)
	String getYahooClientSecret();

	@Option(helpRequest = true)
	boolean getHelp();

}
