package com.enseirb.telecom.dngroup.dvd2c;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletRegistration;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.HelpRequestedException;
import com.lexicalscope.jewel.cli.InvalidOptionSpecificationException;
import com.lexicalscope.jewel.cli.Option;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private static int getPort(int defaultPort) {
		// grab port from environment, otherwise fall back to default port 9998
		return CliConfSingleton.appPort;
	}

	private static final String BASE_PATH = "api";

	/**
	 * Main method .
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {

		try {
			initConfSingleton(args);

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
					CliConfSingleton.appHostName, CliConfSingleton.appPort);
			server.addListener(listener);
			StaticHttpHandler videos = new StaticHttpHandlerCORS(
					new String[] { "/var/www/html/videos" });
			StaticHttpHandler pictures = new StaticHttpHandlerCORS(
					new String[] { "/var/www/html/pictures" });
			StaticHttpHandler cloud = new StaticHttpHandlerCORS(
					new String[] { "/var/www/html/cloud" });

			// set disable cache
			videos.setFileCacheEnabled(false);
			pictures.setFileCacheEnabled(false);
			cloud.setFileCacheEnabled(false);

			server.getServerConfiguration().addHttpHandler(videos, "/videos");
			server.getServerConfiguration().addHttpHandler(pictures,
					"/pictures");
			server.getServerConfiguration().addHttpHandler(cloud, "/cloud");
			server.getServerConfiguration().addHttpHandler(
					new CLStaticHttpHandler(Main.class.getClassLoader(), "/"));

			// finally, deploy the webapp
			webappContext.deploy(server);
			server.start();

			try {
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target(new URI("http://"
						+ CliConfSingleton.appHostName + ":"
						+ CliConfSingleton.appPort + "/api/box"));
				LOGGER.debug("Launch the request to the central : {}",
						target.getUri());

				Response ent = target.request(MediaType.APPLICATION_XML_TYPE)
						.post(null);

				LOGGER.debug("{}", ent);
			} catch (URISyntaxException e) {
				LOGGER.error("URI of server not good");
			}

			LOGGER.info("Jersey app started with WADL available at {}",
					CliConfSingleton.getBaseURI() + "/api/application.wadl");

			// wait for the server to die before we quit
			Thread.currentThread().join();
		} catch (HelpRequestedException ios) {
			System.out.println(ios.getMessage());
		}
	}

	/**
	 * @param args
	 */
	static void initConfSingleton(String[] args) {
		try {
			CliConfiguration cliconf = CliFactory.parseArguments(
					CliConfiguration.class, args);

			CliConfSingleton.boxID = cliconf.getBoxID();
			CliConfSingleton.contentPath = cliconf.getContentPath();
			CliConfSingleton.appHostName = cliconf.getIp();
			CliConfSingleton.publicAddr = cliconf.getPublicAddr();
			CliConfSingleton.rabbitHostname = cliconf.getRabbitHost();
			CliConfSingleton.rabbitPort = cliconf.getRabbitPort();
			CliConfSingleton.appPort = cliconf.getPort();
			CliConfSingleton.database_password = cliconf.getDatabasePassword();
			CliConfSingleton.database_url = cliconf.getDatabaseURL();
			CliConfSingleton.database_username = cliconf.getDatabaseUserName();

			getParametreFromFile();

		} catch (ArgumentValidationException e1) {

			throw e1;

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
			if (CliConfSingleton.appHostName == null)
				CliConfSingleton.appHostName = ApplicationContext
						.getProperties().getProperty("ip");
			if (CliConfSingleton.publicAddr == null)
				CliConfSingleton.publicAddr = ApplicationContext
						.getProperties().getProperty("publicAddr");

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
			if (CliConfSingleton.snapmail_host == null)
				CliConfSingleton.snapmail_host = ApplicationContext
						.getProperties().getProperty("snapmail_host");

			if (CliConfSingleton.database_url == null) {
				CliConfSingleton.database_url = ApplicationContext
						.getProperties().getProperty("database_url");
				LOGGER.debug("CliConfSingleton.database_url = {}",
						CliConfSingleton.database_url);
			} else
				LOGGER.debug("CliConfSingleton.database_url = {}",
						CliConfSingleton.database_url);
			if (CliConfSingleton.database_username == null)
				CliConfSingleton.database_username = ApplicationContext
						.getProperties().getProperty("database_username");
			if (CliConfSingleton.database_password == null)
				CliConfSingleton.database_password = ApplicationContext
						.getProperties().getProperty("database_password");

			LOGGER.info("File found use this values or arg Path ={} ", aPPath);
			in.close();
			CliConfSingleton.defaultValue();

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

	@Option(shortName = "b", longName = "boxID", defaultToNull = true)
	String getBoxID();

	@Option(shortName = "p", longName = "port", description = "the port on which the frontend will listen for http connections", defaultToNull = true)
	Integer getPort();

	@Option(shortName = "i", longName = "ip", description = "the IP on which the frontend will listen for http connections", defaultToNull = true)
	String getIp();

	@Option(longName = "content-path", description = "path of content", defaultToNull = true)
	String getContentPath();

	@Option(shortName = "a", longName = "public-addr", description = "the http addr of curent box", defaultToNull = true)
	String getPublicAddr();

	@Option(longName = "rabbit-host", description = "the host of rabbitMQ", defaultToNull = true)
	String getRabbitHost();

	@Option(longName = "rabbit-port", description = "the port of rabbitMQ", defaultToNull = true)
	Integer getRabbitPort();

	@Option(longName = "database_url", description = "The url of the database (jdbc:mysql://localhost:3306/mediahome)", defaultToNull = true)
	String getDatabaseURL();

	@Option(longName = "database_password", description = "The password of database", defaultToNull = true)
	String getDatabasePassword();

	@Option(longName = "database_username", description = "The Username of database", defaultToNull = true)
	String getDatabaseUserName();

	@Option(helpRequest = true)
	boolean getHelp();

}
