package com.enseirb.telecom.dngroup.dvd2c;

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
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	private static int getPort(int defaultPort) {
		// grab port from environment, otherwise fall back to default port 9998
		return CliConfSingleton.port;
	}

	private static URI getBaseURI() {
		String ip = CliConfSingleton.ip;
		return UriBuilder.fromUri("http://"+ip+"/api/").port(getPort(CliConfSingleton.port))
				.build();
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

		//System.out.println("Starting grizzly2...");
		LOGGER.info("Starting grizzly2");

		LOGGER.info("wadl here -> /api/application.wadl");
		
		// return GrizzlyServerFactory.createHttpServer(BASE_URI,
		// resourceConfig);
		LOGGER.info("Send information to the server central ...");
		try {
			BoxService boxManager = new BoxServiceImpl(new BoxRepositoryMongo("mediahome"));
			boxManager.updateBox();
			
			LOGGER.info("Sucess ");
		} catch (ProcessingException e) {
			LOGGER.error("Error for send information to the server central. Is running ?",e);
		} catch (Exception e) {
			LOGGER.error("Error for send information to the server central.",e);
			
			
		}

		return GrizzlyHttpServerFactory.createHttpServer(getBaseURI(), resources);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		// Properties 
		
		
		CliConfiguration cliconf = CliFactory.parseArguments(CliConfiguration.class, args);

		CliConfSingleton.boxID = cliconf.getBoxID();
		CliConfSingleton.centralURL = cliconf.getCentralURL();
		CliConfSingleton.contentPath= cliconf.getContentPath();
		CliConfSingleton.ip = cliconf.getIp();
		CliConfSingleton.publicAddr = cliconf.getPublicAddr();
		CliConfSingleton.dbHostname = cliconf.getDbHostname();
		CliConfSingleton.dbPort = cliconf.getDbPort();
		CliConfSingleton.rabbitHostname = cliconf.getRabbitHost();
		CliConfSingleton.rabbitPort = cliconf.getRabbitPort();
		CliConfSingleton.port = cliconf.getPort();
		LOGGER.info("the box ID is : {}",CliConfSingleton.boxID);

		
		// Grizzly 2 initialization
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				try {
					
					
					
					
					
					
					HttpServer httpServer = startServer();
//					httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("/var/www"), "/content");
					httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("/var/www/html/videos"), "/videos");
					httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("/var/www/html/pictures"), "/pictures");
					httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("/var/www/html/cloud"), "/cloud");
					
					httpServer.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(Main.class.getClassLoader(), "/"));
					
					
					
							
									
					
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
}

interface CliConfiguration {


	


	@Option(shortName = "b",longName = "boxID", defaultValue = "BOX_TEST")
	String getBoxID();

	@Option(shortName = "p", longName = "port", defaultValue = "9998", description = "the port on which the frontend will listen for http connections")
	Integer getPort();

	@Option( shortName = "i",longName = "ip", defaultValue = "0.0.0.0", description = "the IP on which the frontend will listen for http connections")
	String getIp();
	
	@Option( longName = "content-path", defaultValue = "/var/www/html", description = "path of content")
	String getContentPath();

	@Option(shortName = "c",longName = "central-addr", defaultValue = "http://localhost:9999", description = "the http addr of central server")
	String getCentralURL();

	@Option(shortName = "a",longName = "public-addr", defaultValue = "http://localhost:9998", description = "the http addr of curent box")
	String getPublicAddr();

	@Option(longName = "db-hostname", defaultValue = "localhost", description = "the hostname of database")
	String getDbHostname();

	@Option(longName = "db-port", defaultValue = "27017", description = "the port of database")
	Integer getDbPort();
	
	@Option(longName = "rabbit-host", defaultValue = "localhost", description = "the host of rabbitMQ")
	String getRabbitHost();
	
	@Option(longName = "rabbit-port", defaultValue = "5672", description = "the port of rabbitMQ")
	Integer getRabbitPort();

	@Option(helpRequest = true)
	boolean getHelp();

}
