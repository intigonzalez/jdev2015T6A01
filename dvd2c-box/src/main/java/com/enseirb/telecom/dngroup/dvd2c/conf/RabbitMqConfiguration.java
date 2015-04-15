package com.enseirb.telecom.dngroup.dvd2c.conf;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.service.RabbitMQServiceImpl;
import com.rabbitmq.client.Channel;

@Configuration
public class RabbitMqConfiguration {

	private String QUEUE_NAME = RabbitMQServiceImpl.QUEUE_NAME;

	@Bean
	@Inject
	public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
		RabbitTemplate template = new RabbitTemplate(cf);
		template.setRoutingKey(QUEUE_NAME);
		template.setEncoding("utf-8");

		return template;
	}
	
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
				CliConfSingleton.rabbitHostname);
		connectionFactory.setPort(CliConfSingleton.rabbitPort);

		return connectionFactory;
	}


	@Bean
	public Channel channel() throws IOException {
		Channel channel;
		Connection connection;

		ConnectionFactory factory = connectionFactory();

		connection = factory.createConnection();
		channel = connection.createChannel(true);

		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		return channel;

	}

}