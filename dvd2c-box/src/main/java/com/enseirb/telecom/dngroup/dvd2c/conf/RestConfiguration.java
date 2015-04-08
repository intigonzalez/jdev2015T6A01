package com.enseirb.telecom.dngroup.dvd2c.conf;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.enseirb.telecom.dngroup.dvd2c.endpoints.BoxEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.endpoints.ContentEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.endpoints.RelationEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.endpoints.UserEndPoints;



/**
 * configure the exported resource to rest api
 * 
 * @author dbourasseau
 *
 */
public class RestConfiguration extends ResourceConfig {

	public RestConfiguration() {
		super(MyApplicationEventListener.class);
		// needed for spring injection
		register(RequestContextFilter.class);
		register(ContentEndPoints.class);
		register(BoxEndPoints.class);
		register(RelationEndPoints.class);
		register(UserEndPoints.class);
		

		// tell were to find resources
		// this.packages("fr.labri.progress.comet.endpoint");

	}
}