package com.enseirb.telecom.dngroup.dvd2c.conf;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.mongodb.Mongo;



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
		"com.enseirb.telecom.dngroup.dvd2c.service" })

public class SpringConfiguration {

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper om = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(
				TypeFactory.defaultInstance());
		om.setAnnotationIntrospector(introspector);
		return om;

	}
	/*
	 * Use the standard Mongo driver API to create a com.mongodb.Mongo instance.
	 */
	@Bean
	public Mongo mongo() throws UnknownHostException {
		return new Mongo(CliConfSingleton.dbHostname,CliConfSingleton.dbPort);
	}
	public @Bean MongoTemplate mongoTemplate() throws Exception {
	      return new MongoTemplate(mongo(), "mydatabase");
	  }

}