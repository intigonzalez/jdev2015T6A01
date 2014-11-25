package com.enseirb.telecom.s9;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ResourceFinder;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;

import com.enseirb.telecom.s9.endpoints.ContentEndPoints;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;

import java.io.IOException;
import java.net.URI;

public class Main {

	private static int getPort(int defaultPort) {
		// grab port from environment, otherwise fall back to default port 9998
		String httpPort = System.getProperty("jersey.test.port");
		if (null != httpPort) {
			try {
				return Integer.parseInt(httpPort);
			} catch (NumberFormatException e) {
			}
		}
		return defaultPort;
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/api/").port(getPort(9998))
				.build();
	}

	public static final URI BASE_URI = getBaseURI();

	protected static HttpServer startServer() throws IOException {

		ResourceConfig resources = new ResourceConfig();
		resources.packages("com.enseirb.telecom.s9.endpoints");
		resources.register(MultiPartFeature.class);
		System.out.println("Starting grizzly2...");
		// return GrizzlyServerFactory.createHttpServer(BASE_URI,
		// resourceConfig);
		return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resources);
	}

	public static void main(String[] args) throws IOException {
		// Grizzly 2 initialization
		
		HttpServer httpServer = startServer();
		// httpServer.getServerConfiguration().addHttpHandler(new
		// StaticHttpHandler("display"),"/display");

		httpServer.getServerConfiguration()
				.addHttpHandler(
						new CLStaticHttpHandler(Main.class.getClassLoader(),
								"/"));

		System.out.println(String.format(
				"Jersey app started with WADL available at "
						+ "%sapplication.wadl\n"
						+ "Static files are available at the root: http://localhost:9998/FileUpload.html\nHit enter to stop it...",
				BASE_URI));
		System.in.read();
		httpServer.stop();
	}
}
