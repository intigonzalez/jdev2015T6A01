package com.enseirb.telecom.dngroup.dvd2c.conf;

import org.glassfish.jersey.jettison.JettisonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.enseirb.telecom.dngroup.dvd2c.CORSResponseFilter;
import com.enseirb.telecom.dngroup.dvd2c.endpoints.ContentEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.endpoints.ThridPartyStorageEndPoint;

/**
 * configure the exported resource to rest api
 * 
 * @author dbourasseau
 *
 */
public class RestConfiguration extends ResourceConfig {

	public RestConfiguration() {
		super();// super(MyApplicationEventListener.class);
		// endpoints

		register(ContentEndPoints.class);

		register(ThridPartyStorageEndPoint.class);
		register(MultiPartFeature.class);
		register(JettisonFeature.class);
		register(RequestContextFilter.class);
		register(CORSResponseFilter.class);

	}
}