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

import com.enseirb.telecom.s9.User;

public class RequetServiceImpl implements RequetService {

	public static String doGet(String url) throws IOException {

		String source = "";
		URL oracle = new URL(url);
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			source += inputLine;
		in.close();
		return source;
	}

	public static String post(String adress,User user) throws IOException{

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(adress);
		 
		 
		User bean = target.request(MediaType.APPLICATION_XML_TYPE).post(Entity.entity(user,MediaType.APPLICATION_XML),User.class);

		return adress;
		 
//		HttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost("http://www.baidu.com");
//        String xml = "<xml>xxxx</xml>";
//        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
//        post.setEntity(entity);
//        HttpResponse response = client.execute(post);
//        String result = EntityUtils.toString(response.getEntity());
        
	}
	
}
