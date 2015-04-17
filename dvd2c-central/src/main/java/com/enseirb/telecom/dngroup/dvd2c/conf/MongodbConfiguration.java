package com.enseirb.telecom.dngroup.dvd2c.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.enseirb.telecom.dngroup.dvd2c.db")
public class MongodbConfiguration   {



}