package com.enseirb.telecom.dngroup.snapmail.server.service;

import javax.ws.rs.core.Response;

public interface SnapmailService {

	/**
	 * Redirect the Response to the oauth server associated with the service
	 * 
	 * @param service
	 * @param actorID
	 * @return Response
	 */
	public abstract Response redirectOauthService(String service, String actorID);

	/**
	 * Request the Oauth refresh token with the authorization code
	 * 
	 * @param service
	 * @param code
	 * @param userID
	 * @return Response
	 */
	public abstract Response getOauthTokenWithCode(String service, String code,
			String userID);

}
