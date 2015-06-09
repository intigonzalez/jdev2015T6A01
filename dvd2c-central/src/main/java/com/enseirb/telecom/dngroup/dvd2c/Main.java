package com.enseirb.telecom.dngroup.dvd2c;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import javax.servlet.ServletRegistration;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.HelpRequestedException;
import com.lexicalscope.jewel.cli.InvalidOptionSpecificationException;
import com.lexicalscope.jewel.cli.Option;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	// public static final String BASE_PATH = "api";
	private static int getPort(int defaultPort) {
		// grab port from environment, otherwise fall back to default port 9998
		return CliConfSingleton.appPort;
	}

	private static final String BASE_PATH = "api";

	private static URI getBaseApiURI() {

		return UriBuilder.fromUri(
				"http://" + CliConfSingleton.appHostName + ":"
						+ getPort(CliConfSingleton.appPort)).build();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {

		try {
			initConfSingleton(args);

			String baseHost = CliConfSingleton.appHostName;
			int pasePort = CliConfSingleton.appPort;

			// WEB APP SETUP

			// instead of using web.xml, we use java-based configuration
			WebappContext webappContext = new WebappContext("production");

			// add a listener to spring so that IoC can happen
			webappContext.addListener(ContextLoaderListener.class);

			// specify that spring should be configured with annotations
			webappContext.addContextInitParameter(
					ContextLoader.CONTEXT_CLASS_PARAM,
					AnnotationConfigWebApplicationContext.class.getName());

			// and where spring should find its configuration
			webappContext
					.addContextInitParameter(
							ContextLoader.CONFIG_LOCATION_PARAM,
							com.enseirb.telecom.dngroup.dvd2c.conf.SpringConfiguration.class
									.getName());
			// attache the jersey servlet to this context
			ServletRegistration jerseyServlet = webappContext.addServlet(
					"jersey-servlet", ServletContainer.class);

			// configure it with extern configuration class
			jerseyServlet
					.setInitParameter(
							"javax.ws.rs.Application",
							com.enseirb.telecom.dngroup.dvd2c.conf.RestConfiguration.class
									.getName());

			// finally, map it to the path
			jerseyServlet.addMapping("/" + BASE_PATH + "/*");

			// add mapping for static resources

			// start a vanilla server
			HttpServer server = new HttpServer();

			// configure a network listener with our configuration
			NetworkListener listener = new NetworkListener("grizzly2",
					baseHost, pasePort);
			server.addListener(listener);

			server.getServerConfiguration().addHttpHandler(
					new CLStaticHttpHandler(Main.class.getClassLoader(), "/"));

			// finally, deploy the webapp
			webappContext.deploy(server);
			server.start();

			LOGGER.info("Jersey app started with WADL available at {}",
					getBaseApiURI() + "/api/application.wadl");

			// wait for the server to die before we quit
			Thread.currentThread().join();
		} catch (HelpRequestedException ios) {
			System.out.println(ios.getMessage());
		}
	}

	// protected static HttpServer startServer() throws IOException {
	//
	// ResourceConfig resources = new ResourceConfig();
	// resources.packages("com.enseirb.telecom.dngroup.dvd2c.endpoints");
	// resources.register(CORSResponseFilter.class);
	// resources.register(MultiPartFeature.class);
	// resources.register(JettisonFeature.class);
	// /**
	// * this two follow line is for security
	// */
	// resources.register(SecurityEntityFilteringFeature.class);
	//
	// resources.register(SecurityRequestFilter.class);
	//
	// // System.out.println("Starting grizzly2...");
	// LOGGER.info("Starting grizzly2");
	//
	// LOGGER.info("wadl here -> /api/application.wadl");
	//
	// // return GrizzlyServerFactory.createHttpServer(BASE_URI,
	// // resourceConfig);
	// LOGGER.info("Send information to the server central ...");
	// try {
	// BoxService boxManager = new BoxServiceImpl(new BoxRepositoryMongo(
	// "mediahome"));
	// boxManager.updateBox();
	//
	// LOGGER.info("Sucess ");
	// } catch (ProcessingException e) {
	// LOGGER.error(
	// "Error for send information to the server central. Is running ?",
	// e);
	// } catch (Exception e) {
	// LOGGER.error("Error for send information to the server central.", e);
	//
	// }
	//
	// // WEB APP SETUP
	// LOGGER.debug("WEB APP SETUP");
	// // instead of using web.xml, we use java-based configuration
	// WebappContext webappContext = new WebappContext("production");
	//
	// // add a listener to spring so that IoC can happen
	// webappContext.addListener(ContextLoaderListener.class);
	//
	// // specify that spring should be configured with annotations
	// webappContext.addContextInitParameter(
	// ContextLoader.CONTEXT_CLASS_PARAM,
	// AnnotationConfigWebApplicationContext.class.getName());
	//
	// // and where spring should find its configuration
	// webappContext.addContextInitParameter(
	// ContextLoader.CONFIG_LOCATION_PARAM,
	// SpringConfiguration.class.getName());
	// // attache the jersey servlet to this context
	// ServletRegistration jerseyServlet = webappContext.addServlet(
	// "jersey-servlet", ServletContainer.class);
	//
	// // configure it with extern configuration class
	// jerseyServlet.setInitParameter("javax.ws.rs.Application",
	// RestConfiguration.class
	// .getName());
	//
	// HttpServer httpServer =
	// GrizzlyHttpServerFactory.createHttpServer(getBaseURI(),
	// resources);
	//
	// httpServer.getServerConfiguration().addHttpHandler(
	// new StaticHttpHandler("/var/www/html/videos"),
	// "/videos");
	// httpServer.getServerConfiguration().addHttpHandler(
	// new StaticHttpHandler("/var/www/html/pictures"),
	// "/pictures");
	// httpServer.getServerConfiguration().addHttpHandler(
	// new StaticHttpHandler("/var/www/html/cloud"),
	// "/cloud");
	//
	// httpServer.getServerConfiguration().addHttpHandler(
	// new CLStaticHttpHandler(
	// Main.class.getClassLoader(), "/"));
	//
	// // configure a network listener with our configuration
	// NetworkListener listener = new NetworkListener("grizzly2",
	// CliConfSingleton.ip, CliConfSingleton.port);
	// httpServer.addListener(listener);
	//
	// // finally, deploy the webapp
	// webappContext.deploy(httpServer);
	//
	//
	// return httpServer;
	// }

	// public static void main(String[] args) throws IOException,
	// InterruptedException {
	// // Properties
	//
	// initConfSingleton(args);
	//
	// LOGGER.info("the box ID is : {}", CliConfSingleton.boxID);
	//
	// // Grizzly 2 initialization
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	//
	// try {
	//
	// HttpServer httpServer = startServer();
	//
	// } catch (IOException e) {
	// throw Throwables.propagate(e);
	// }
	// }
	// }).start();
	//
	// try {
	// Thread.currentThread().join();
	// } catch (InterruptedException e) {
	// Thread.currentThread().interrupt();
	// throw e;
	// }
	//
	// // httpServer.stop();
	// }

	/**
	 * @param args
	 */
	static void initConfSingleton(String[] args) {
		try {
			CliConfiguration cliconf = CliFactory.parseArguments(
					CliConfiguration.class, args);
			CliConfSingleton.appHostName = cliconf.getIp();
			CliConfSingleton.dbHostname = cliconf.getDbHostname();
			CliConfSingleton.dbPort = cliconf.getDbPort();
			CliConfSingleton.appPort = cliconf.getPort();
		} catch (ArgumentValidationException e1) {

			LOGGER.info("No arg detected use default or file value ");
			getParametreFromFile();

		} catch (InvalidOptionSpecificationException e1) {
			LOGGER.info("False arg detected use default or file value ");
			getParametreFromFile();
		}
	}

	/**
	 * 
	 */
	static void getParametreFromFile() {
		String aPPath = "/etc/mediahome/central.properties";
		try {
			FileInputStream in = new FileInputStream(aPPath);
			ApplicationContext.properties.load(in);

			if (CliConfSingleton.appHostName == null)
				CliConfSingleton.appHostName = ApplicationContext
						.getProperties().getProperty("ip");
			if (CliConfSingleton.dbHostname == null)
				CliConfSingleton.dbHostname = ApplicationContext
						.getProperties().getProperty("dbHostname");
			if (CliConfSingleton.dbPort == null)
				CliConfSingleton.dbPort = Integer.valueOf(ApplicationContext
						.getProperties().getProperty("dbPort"));
			if (CliConfSingleton.appPort == null)
				CliConfSingleton.appPort = Integer.valueOf(ApplicationContext
						.getProperties().getProperty("port"));
			
		} catch (FileNotFoundException e1) {
			LOGGER.info("File not found use default value or arg Path ={} ",
					aPPath);
			CliConfSingleton.defaultValue();
		} catch (Exception e1) {
			LOGGER.info("File error not complete ", aPPath);
			CliConfSingleton.defaultValue();
		}
	}

	// private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	// private static int getPort(int defaultPort) {
	// // grab port from environment, otherwise fall back to default port 9999
	//
	// return CliConfSingleton.appPort;
	// }
	//
	// private static URI getBaseURI() {
	// String ip = CliConfSingleton.appHostName;
	// return UriBuilder.fromUri("http://" + ip + "/api/").port(getPort(9999))
	// .build();
	// }
	//
	// protected static HttpServer startServer() throws IOException {
	//
	// ResourceConfig resources = new ResourceConfig();
	// resources.packages("com.enseirb.telecom.dngroup.dvd2c.endpoints");
	// resources.register(CORSResponseFilter.class);
	// resources.register(MultiPartFeature.class);
	// resources.register(JettisonFeature.class);
	// System.out.println("Starting grizzly2...");
	// // return GrizzlyServerFactory.createHttpServer(BASE_URI,
	// // resourceConfig);
	// return GrizzlyHttpServerFactory.createHttpServer(getBaseURI(),
	// resources);
	// }
	//
	// public static void main(String[] args) throws IOException,
	// InterruptedException {
	// // Properties
	// CliConfiguration cliconf = CliFactory.parseArguments(
	// CliConfiguration.class, args);
	//
	// CliConfSingleton.appHostName = cliconf.getIp();
	// CliConfSingleton.appPort = cliconf.getPort();
	// CliConfSingleton.dbHostname = cliconf.getDbHostname();
	// CliConfSingleton.dbPort = cliconf.getDbPort();
	// // FileInputStream in;
	// // ApplicationContext.properties = new Properties();
	// // //try to find application.properties
	// //
	// // String aPPath = new String();
	// // if (args.length>0) aPPath = args[0];
	// // else aPPath = "/etc/mediahome/central.properties";
	//
	// // try {
	// // in = new FileInputStream(aPPath);
	// // ApplicationContext.properties.load(in);
	// // in.close();
	// // } catch (FileNotFoundException e1) {
	// // LOGGER.error("File not found Path ={} ",aPPath, e1);
	// // return;
	// // }
	// // LOGGER.debug("File Found Path={} ",aPPath);
	//
	//
	// // Grizzly 2 initialization
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	//
	// try {
	// HttpServer httpServer = startServer();
	// httpServer.getServerConfiguration().addHttpHandler(
	// new CLStaticHttpHandler(
	// Main.class.getClassLoader(), "/"));
	//
	// } catch (IOException e) {
	// throw Throwables.propagate(e);
	// }
	// }
	// }).start();
	//
	// Thread.currentThread().join();
	//
	// // httpServer.stop();
	// }
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
