package com.enseirb.telecom.dngroup.dvd2c;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jettison.JettisonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.HelpRequestedException;
import com.lexicalscope.jewel.cli.InvalidOptionSpecificationException;
import com.lexicalscope.jewel.cli.Option;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	private static int getPort(int defaultPort) {
		// grab port from environment, otherwise fall back to default port 9999
		
		return CliConfSingleton.port;
	}

	private static URI getBaseURI() {
		String ip = CliConfSingleton.ip;
		return UriBuilder.fromUri("http://" + ip + "/api/").port(getPort(9999))
				.build();
	}

	protected static HttpServer startServer() throws IOException {

		ResourceConfig resources = new ResourceConfig();
		resources.packages("com.enseirb.telecom.dngroup.dvd2c.endpoints");
		resources.register(CORSResponseFilter.class);
		resources.register(MultiPartFeature.class);
		resources.register(JettisonFeature.class);
		System.out.println("Starting grizzly2...");
		// return GrizzlyServerFactory.createHttpServer(BASE_URI,
		// resourceConfig);
		return GrizzlyHttpServerFactory.createHttpServer(getBaseURI(),
				resources);
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// Properties
		CliConfiguration cliconf = CliFactory.parseArguments(
				CliConfiguration.class, args);

		CliConfSingleton.ip = cliconf.getIp();
		CliConfSingleton.port = cliconf.getPort();
		CliConfSingleton.dbHostname = cliconf.getDbHostname();
		CliConfSingleton.dbPort = cliconf.getDbPort();
//		FileInputStream in;
//		ApplicationContext.properties = new Properties();
//		//try to find application.properties
//		
//		String aPPath = new String();
//		if (args.length>0)	aPPath = args[0];		
//		else aPPath = "/etc/mediahome/central.properties";

//		try {
//			in = new FileInputStream(aPPath);
//			ApplicationContext.properties.load(in);
//			in.close();
//		} catch (FileNotFoundException e1) {
//			LOGGER.error("File not found Path ={} ",aPPath, e1);
//			return;
//		}
//		LOGGER.debug("File Found Path={} ",aPPath);

		
		// Grizzly 2 initialization
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					HttpServer httpServer = startServer();
					httpServer.getServerConfiguration().addHttpHandler(
							new CLStaticHttpHandler(
									Main.class.getClassLoader(), "/"));

				} catch (IOException e) {
					throw Throwables.propagate(e);
				}
			}
		}).start();

		Thread.currentThread().join();

		// httpServer.stop();
	}
}

interface CliConfiguration {
	
	@Option(shortName = "p", longName = "port", defaultValue = "9999", description = "the port on which the frontend will listen for http connections")
	Integer getPort();

	@Option(shortName = "ip", longName = "ip", defaultValue = "0.0.0.0", description = "the IP on which the frontend will listen for http connections")
	String getIp();
	

	@Option(longName = "db-hostname", defaultValue = "localhost", description = "the hostname of database")
	String getDbHostname();

	@Option(longName = "db-port", defaultValue = "27017", description = "the port of database")
	Integer getDbPort();

	

	@Option(helpRequest = true)
	boolean getHelp();
	}
