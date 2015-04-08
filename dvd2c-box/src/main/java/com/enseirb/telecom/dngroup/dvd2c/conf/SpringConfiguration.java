package com.enseirb.telecom.dngroup.dvd2c.conf;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.core.JsonFactory;
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
@ComponentScan(basePackages = { "com.enseirb.telecom.dngroup.dvd2c",
		"com.enseirb.telecom.dngroup.dvd2c.db", "com.enseirb.telecom.dngroup.dvd2c.conf" })
@EnableJpaRepositories("fr.labri.progress.comet.repository")
@Import(RabbitMqConfiguration.class)
public class SpringConfiguration {

	
	@Bean
	public ObjectMapper getObjectMapper(){
		ObjectMapper om = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(
				TypeFactory.defaultInstance());
		om.setAnnotationIntrospector(introspector);
		return om;
		
	}
	


	@Bean
	public DataSource ds() {

		return new JDBCDataSource();

	}

	@Bean(name = "transactionManager")
	@Inject
	public PlatformTransactionManager tm(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean(name = "entityManagerFactory")
	@Inject
	public EntityManagerFactory emf() {

		return Persistence.createEntityManagerFactory("cache-orchestrator");
	}

}