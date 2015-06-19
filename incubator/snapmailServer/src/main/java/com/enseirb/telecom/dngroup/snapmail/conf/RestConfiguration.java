package com.enseirb.telecom.dngroup.snapmail.conf;

import org.glassfish.jersey.jettison.JettisonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.enseirb.telecom.dngroup.snapmail.endpoints.SnapmailEndPoints;


/**
 * configure the exported resource to rest api
 * 
 * @author dbourasseau
 *
 */
public class RestConfiguration extends ResourceConfig {

	public RestConfiguration() {
	
		// endpoints
	
		register(SnapmailEndPoints.class);

		// features
		register(MultiPartFeature.class);
		register(JettisonFeature.class);
		register(RequestContextFilter.class);

	}
}