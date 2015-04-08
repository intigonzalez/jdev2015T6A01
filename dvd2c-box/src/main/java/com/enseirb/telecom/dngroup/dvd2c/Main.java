package com.enseirb.telecom.dngroup.dvd2c;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.enseirb.telecom.dngroup.dvd2c.conf.SpringConfiguration;
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
				"http://"+CliConfSingleton.appHostName + ":"
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
			webappContext.addContextInitParameter(
					ContextLoader.CONFIG_LOCATION_PARAM,
					SpringConfiguration.class.getName());
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

			// start a vanilla server
			HttpServer server = new HttpServer();

			// configure a network listener with our configuration
			NetworkListener listener = new NetworkListener("grizzly2",
					baseHost, pasePort);
			server.addListener(listener);

			// finally, deploy the webapp
			webappContext.deploy(server);
			server.start();

			LOGGER.info("Jersey app started with WADL available at {}",
					getBaseApiURI() + "/application.wadl");

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
	// // httpServer.getServerConfiguration().addHttpHandler(new
	// // StaticHttpHandler("/var/www"), "/content");
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

			CliConfSingleton.boxID = cliconf.getBoxID();
			CliConfSingleton.centralURL = cliconf.getCentralURL();
			CliConfSingleton.contentPath = cliconf.getContentPath();
			CliConfSingleton.appHostName = cliconf.getIp();
			CliConfSingleton.publicAddr = cliconf.getPublicAddr();
			CliConfSingleton.dbHostname = cliconf.getDbHostname();
			CliConfSingleton.dbPort = cliconf.getDbPort();
			CliConfSingleton.rabbitHostname = cliconf.getRabbitHost();
			CliConfSingleton.rabbitPort = cliconf.getRabbitPort();
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
			if (CliConfSingleton.appHostName == null)
				CliConfSingleton.appHostName = ApplicationContext
						.getProperties().getProperty("ip");
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
			if (CliConfSingleton.appPort == null)
				CliConfSingleton.appPort = Integer.valueOf(ApplicationContext
						.getProperties().getProperty("port"));
			LOGGER.info("File found = {} ", aPPath);
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

	@Option(shortName = "b", longName = "boxID")
	String getBoxID();

	@Option(shortName = "p", longName = "port", description = "the port on which the frontend will listen for http connections")
	Integer getPort();

	@Option(shortName = "i", longName = "ip", description = "the IP on which the frontend will listen for http connections")
	String getIp();

	@Option(longName = "content-path", description = "path of content")
	String getContentPath();

	@Option(shortName = "c", longName = "central-addr", description = "the http addr of central server")
	String getCentralURL();

	@Option(shortName = "a", longName = "public-addr", description = "the http addr of curent box")
	String getPublicAddr();

	@Option(longName = "db-hostname", description = "the hostname of database")
	String getDbHostname();

	@Option(longName = "db-port", description = "the port of database")
	Integer getDbPort();

	@Option(longName = "rabbit-host", description = "the host of rabbitMQ")
	String getRabbitHost();

	@Option(longName = "rabbit-port", description = "the port of rabbitMQ")
	Integer getRabbitPort();

	@Option(helpRequest = true)
	boolean getHelp();

}
