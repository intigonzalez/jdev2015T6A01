package com.enseirb.telecom.dngroup.snapmail.endpoints;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;
import com.enseirb.telecom.dngroup.snapmail.service.SnapmailService;

// The Java class will be hosted at the URI path "/"

@SuppressWarnings("serial")
@Path("/")
public class SnapmailEndPoints extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SnapmailEndPoints.class);

	@Inject
	SnapmailService snapmailservice;

	/**
	 * Send a request to the box to get a content
	 * 
	 * @param actorID
	 * @param contentID
	 * @return content
	 */
	@GET
	@Path("/app/{actorID}/content/{contentID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Content getBoxContent(@PathParam("actorID") String actorID,
			@PathParam("contentID") String contentID) {
		Content content;
		Client client = ClientBuilder.newClient();
		//TODO: à remodifier après sécurité
		String url = CliConfSingleton.mediahome_host.toString()
				+ "/api/app/content/unsecure/" + contentID + "/" + actorID;
		WebTarget target = client.target(url);
		try {
			content = target.request(MediaType.APPLICATION_XML_TYPE).get(
					Content.class);
			client.close();
			if (!content.getLink().contains(CliConfSingleton.mediahome_host)) {
				content.setLink(CliConfSingleton.mediahome_host
						+ content.getLink());
			}
			return content;
		} catch (ProcessingException e) {
			client.close();
			LOGGER.error("no content");
		}
		return null;

	}

	/**
	 * Redirect the client to google, yahoo or microsoft identification and
	 * authorization system
	 * 
	 * @param actorID
	 * @param service
	 *            (google or yahoo)
	 * @throws URISyntaxException
	 * @return 302 if the service is known, or 404
	 */
	@GET
	@Path("oauth/{actorID}/{service}")
	public Response getOauthredirect(@PathParam("actorID") String actorID,
			@PathParam("service") String service) throws URISyntaxException {

		return snapmailservice.redirectOauthService(service, actorID);
	}

	/**
	 * Request a refresh_token to google or yahoo thanks to the authorization
	 * code and save this token
	 * 
	 * @param actorID
	 * @param code
	 * @throw URISyntaxException
	 * @return an error if google or yahoo don't give a token, a "ok" if the
	 *         token is saved
	 */

	@POST
	@Path("oauth/{actorID}")
	@Consumes("text/plain")
	public Response postOauth(@PathParam("actorID") String actorID, String code)
			throws URISyntaxException {

		String userID = actorID.substring(0, actorID.indexOf("_"));
		String service = actorID.substring(actorID.indexOf("_") + 1, actorID.lastIndexOf("_"));
		String validator = actorID.substring(actorID.lastIndexOf("_") + 1);
		return snapmailservice.getOauthTokenWithCode(service, code, userID, validator);

	}

	/**
	 * Redirect a user who wants to log in to his box webpage. His box is known
	 * thanks to a request to the Central server.
	 * 
	 * @param userID
	 * @return Response
	 */
	@GET
	@Path("app/login/{userID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response logInMediaHome(@PathParam("userID") String userID) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(CliConfSingleton.centralURL
				+ "/api/oauth/box/" + userID);
		String redirect = target.request().get(String.class);
		try {
			return Response.seeOther(new URI(redirect + "/home.html#/friends")).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
