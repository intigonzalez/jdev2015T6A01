package com.enseirb.telecom.s9.service;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.Response.Status;

import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public class RequetUserServiceImpl implements RequetUserService {

	private String url;

	public RequetUserServiceImpl(String url) {
		this.url = url;
	}
	
	@Override
	public Response get(String url) {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.get();
		return response;
	}

	@Override
	public Response post(String url, User user) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.post(Entity.entity(user, MediaType.APPLICATION_XML),
						Response.class);
		return response;
	}

	@Override
	public Response put(String url, User user) {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.post(Entity.entity(user, MediaType.APPLICATION_XML),
						Response.class);
		return response;
	}



	@Override
	public void delete(User user) throws IOException, NoSuchUserException {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(this.url);
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.delete();

		switch (Status.valueOf("" + response.getStatus())) {
		case ACCEPTED:
			break;
		case BAD_GATEWAY:
			break;
		case BAD_REQUEST:
			break;
		case CONFLICT:
			break;
		case CREATED:
			break;
		case EXPECTATION_FAILED:
			break;
		case FORBIDDEN:
			break;
		case FOUND:
			break;
		case GATEWAY_TIMEOUT:
			break;
		case GONE:
			break;
		case HTTP_VERSION_NOT_SUPPORTED:
			break;
		case INTERNAL_SERVER_ERROR:
			break;
		case LENGTH_REQUIRED:
			break;
		case METHOD_NOT_ALLOWED:
			break;
		case MOVED_PERMANENTLY:
			break;
		case NOT_ACCEPTABLE:
			break;
		case NOT_FOUND:
			break;
		case NOT_IMPLEMENTED:
			break;
		case NOT_MODIFIED:
			break;
		case NO_CONTENT:
			break;
		case OK:
			break;
		case PARTIAL_CONTENT:
			break;
		case PAYMENT_REQUIRED:
			break;
		case PRECONDITION_FAILED:
			break;
		case PROXY_AUTHENTICATION_REQUIRED:
			break;
		case REQUESTED_RANGE_NOT_SATISFIABLE:
			break;
		case REQUEST_ENTITY_TOO_LARGE:
			break;
		case REQUEST_TIMEOUT:
			break;
		case REQUEST_URI_TOO_LONG:
			break;
		case RESET_CONTENT:
			break;
		case SEE_OTHER:
			break;
		case SERVICE_UNAVAILABLE:
			break;
		case TEMPORARY_REDIRECT:
			break;
		case UNAUTHORIZED:
			break;
		case UNSUPPORTED_MEDIA_TYPE:
			break;
		case USE_PROXY:
			break;
		default:
			break;

		}

	}

}
