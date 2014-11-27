package com.enseirb.telecom.s9;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jettison.JettisonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.google.common.base.Throwables;

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
		return UriBuilder.fromUri("http://0.0.0.0/api/").port(getPort(9998))
				.build();
	}

	public static final URI BASE_URI = getBaseURI();

	protected static HttpServer startServer() throws IOException {

		ResourceConfig resources = new ResourceConfig();
		resources.packages("com.enseirb.telecom.s9.endpoints");
		resources.register(CORSResponseFilter.class);
		resources.register(MultiPartFeature.class);
		resources.register(JettisonFeature.class);
		System.out.println("Starting grizzly2...");
		// return GrizzlyServerFactory.createHttpServer(BASE_URI,
		// resourceConfig);
		return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resources);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
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
				// httpServer.getServerConfiguration().addHttpHandler(new
				// StaticHttpHandler("display"),"/display");

			}
		}).start();
		
		Thread.currentThread().join();

		// httpServer.stop();
	}
}
