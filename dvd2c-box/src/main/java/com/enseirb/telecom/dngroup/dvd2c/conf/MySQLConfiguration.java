package com.enseirb.telecom.dngroup.dvd2c.conf;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.spr")
@EnableJpaRepositories("com.enseirb.telecom.dngroup.dvd2c")
class MySQLConfiguration {

	private static final String PROPERTY_NAME_DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private static final String PROPERTY_NAME_DATABASE_PASSWORD = "mediahome";
	private static final String PROPERTY_NAME_DATABASE_URL = "jdbc:mysql://localhost:3306/mediahome";
	private static final String PROPERTY_NAME_DATABASE_USERNAME = "mediahome";

	private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "com.enseirb.telecom.dngroup.dvd2c";



	@Bean
	public DataSource dataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(PROPERTY_NAME_DATABASE_DRIVER);
		dataSource.setUrl(PROPERTY_NAME_DATABASE_URL);
		dataSource.setUsername(PROPERTY_NAME_DATABASE_USERNAME);
		dataSource.setPassword(PROPERTY_NAME_DATABASE_PASSWORD);

		return dataSource;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN);
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}

}
