package com.enseirb.telecom.dngroup.snapmail.server;

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

import com.enseirb.telecom.dngroup.snapmail.cli.ApplicationContext;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConf;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConfArgs;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;
import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.HelpRequestedException;
import com.lexicalscope.jewel.cli.InvalidOptionSpecificationException;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	private static final String BASE_PATH = "api";

	private static URI getBaseApiURI() {

		return UriBuilder.fromUri(
				"http://" + "localhost" + ":"
						+ "9997").build();
	}

	
	public static void main(String[] args){
		CliConfArgs.getParametreFromArgs(args);
		try {
			startGrizzlyServer(CliConfSingleton.snapmail_host);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void startGrizzlyServer(String host) throws IOException, InterruptedException {
		try {
			

			String baseHost = host.substring(host.lastIndexOf("http://")+7, host.lastIndexOf(":"));
			LOGGER.debug(host.substring(host.lastIndexOf("http://")+7, host.lastIndexOf(":")));
			int pasePort = Integer.parseInt(host.substring(host.lastIndexOf(":")+1));

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
							com.enseirb.telecom.dngroup.snapmail.server.conf.SpringConfiguration.class
									.getName());
			// attache the jersey servlet to this context
			ServletRegistration jerseyServlet = webappContext.addServlet(
					"jersey-servlet", ServletContainer.class);

			// configure it with extern configuration class
			jerseyServlet
					.setInitParameter(
							"javax.ws.rs.Application",
							com.enseirb.telecom.dngroup.snapmail.server.conf.RestConfiguration.class
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
					getBaseApiURI() + "/application.wadl");

			// wait for the server to die before we quit
			Thread.currentThread().join();
		} catch (HelpRequestedException ios) {
			System.out.println(ios.getMessage());
		}
	}
}
