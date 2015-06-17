package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;

@Path("snapmail")
public class SnapmailEndPoints {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SnapmailEndPoints.class);
	private static final long max_time = 10000;
	
	private static String personalToken = "";
	private static Authentication auth = null;
	private static long timer = 0;
	
	@Inject
	protected AccountService uManager;

	@GET
	@Path("/oauth/{actorID}/{service}")
	public Response redirectOauthService(@PathParam("actorID") String actorID,
			@PathParam("service") String service) {
		 auth = SecurityContextHolder.getContext()
				 .getAuthentication();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(CliConfSingleton.snapmail_host
				+ "/api/oauth/" + actorID + "/" + service);
		Response response = target.request().get(Response.class);
		client.close();

		return response;
	}

	@POST
	@Path("/oauth/{actorID}")
	@Consumes("text/plain")
	public Response redirectOauthCode(@PathParam("actorID") String actorID,
			String code) {
		
		timer = new Date().getTime();
		personalToken= UUID.randomUUID().toString();
		actorID=actorID.concat("_" + personalToken);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(CliConfSingleton.snapmail_host
				+ "/api/oauth/" + actorID);
		Response response = target.request().post(
				Entity.entity(code, "text/plain"), Response.class);
		client.close();
		return response;
	}

	@PUT
	@Path("/properties/{userID}/{validator}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response redirectNewOauthProperties(PropertyGroups propertyGroups, @PathParam("userID") String userID, @PathParam("validator") String validator) {
		// TODO: add verif
		
		long time =new Date().getTime();
		if(time < timer+max_time && personalToken.contentEquals(validator)){
		//Get user with auth if validator ok
		 UUID uuid = UUID.fromString(auth.getName());
		 auth=null; timer= 0; personalToken="";
		 User user = null;
		try {
			user = uManager.findUserByUUID(uuid);
		} catch (NoSuchUserException e) {
			e.printStackTrace();
		}
		 HttpAuthenticationFeature feature = HttpAuthenticationFeature
				 .basic(user.getEmail(), user.getEncryptedPassword());
		Client client = ClientBuilder.newClient();
		 client.register(feature).register(MultiPartFeature.class);
//		WebTarget target = client.target(CliConfSingleton.publicAddr
//				+ "/api/app/unsecure/properties/" + userID);
		WebTarget target = client.target(CliConfSingleton.publicAddr
				+ "/api/app/properties");
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.put(Entity.entity(propertyGroups, MediaType.APPLICATION_XML),
						Response.class);
		return response;
		}
		else {
			auth=null;auth=null; timer= 0; personalToken="";
			return Response.status(405).build();
		}
	}
}