package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

@Service
@Deprecated
public class RabbitMQServiceImpl implements MessageBrokerService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RabbitMQServiceImpl.class);
	

	@Inject
	ContentService contentService;

	@Inject
	volatile Channel channel;

	@Inject
	QueueConsumerAppImp queueConsumerApp;
	

	public static String QUEUE_NAME = "celery";
	
	public RabbitMQServiceImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.dngroup.dvd2c.service.RabbitMQService#addTask(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public void addTask(String task, Integer id)
			throws UnsupportedEncodingException, IOException {
		
		channel.basicPublish("", QUEUE_NAME, new AMQP.BasicProperties.Builder()
				.contentType("application/json").contentEncoding("utf-8")
				.build(), task.getBytes("utf-8"));

		try {
			queueConsumerApp.getQueueMessage(id.toString(), contentService);
		} catch (InterruptedException e) {
			LOGGER.error("Queue interrupted", e);
		}
	}
}
