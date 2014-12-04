package com.enseirb.telecom.s9.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.enseirb.telecom.s9.User;

public class RequetServiceImpl implements RequetService {

	public static Response get(String url) {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);		 
		 Response response = target.request(MediaType.APPLICATION_XML_TYPE).get();//Entity.entity(user,MediaType.APPLICATION_XML),Response.class);
		return response;
	}

	public static Response post(String url,User user){
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);		 
		Response response = target.request(MediaType.APPLICATION_XML_TYPE).post(Entity.entity(user,MediaType.APPLICATION_XML),Response.class);
		return response;
	}
	
}
