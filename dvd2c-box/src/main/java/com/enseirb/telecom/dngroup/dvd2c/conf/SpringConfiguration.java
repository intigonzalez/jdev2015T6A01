package com.enseirb.telecom.dngroup.dvd2c.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
		"com.enseirb.telecom.dngroup.dvd2c.service" })
@Import(RabbitMqConfiguration.class)
public class SpringConfiguration {

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper om = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(
				TypeFactory.defaultInstance());
		om.setAnnotationIntrospector(introspector);
		return om;

	}

}