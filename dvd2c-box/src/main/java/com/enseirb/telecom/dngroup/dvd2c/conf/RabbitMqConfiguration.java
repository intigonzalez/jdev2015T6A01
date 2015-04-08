package com.enseirb.telecom.dngroup.dvd2c.conf;

import javax.inject.Inject;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;

@Configuration
public class RabbitMqConfiguration {

	protected final String celeryQueueName = "celery";

	@Bean
	@Inject
	public RabbitAdmin getAdmin(ConnectionFactory cf) {
		return new RabbitAdmin(cf);
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
				CliConfSingleton.rabbitHostname);
		connectionFactory.setPort(CliConfSingleton.rabbitPort);

		return connectionFactory;
	}

	@Bean
	@Inject
	public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
		RabbitTemplate template = new RabbitTemplate(cf);
		template.setRoutingKey(this.celeryQueueName);
		template.setEncoding("utf-8");

		return template;
	}

}