package com.enseirb.telecom.dngroup.dvd2c.conf;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * this class is responsible for configuring spring context and repositories
 * 
 * @author nherbaut
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.enseirb.telecom.dngroup.dvd2c.filter",
		"com.enseirb.telecom.dngroup.dvd2c.db",
		"com.enseirb.telecom.dngroup.dvd2c.conf",
		"com.enseirb.telecom.dngroup.dvd2c.service",
		"com.enseirb.telecom.dngroup.dvd2c.endpoints",
		"com.enseirb.telecom.dngroup.dvd2c.security",
		"com.enseirb.telecom.dngroup.dvd2c.repository",
		"com.enseirb.telecom.dngroup.dvd2c"})
@EnableTransactionManagement
public class SpringConfiguration {

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper om = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(
				TypeFactory.defaultInstance());
		om.setAnnotationIntrospector(introspector);
		return om;

	}

	@Bean
	public Client client() {
		Client client = ClientBuilder.newClient();
		return client;
		
	}
}