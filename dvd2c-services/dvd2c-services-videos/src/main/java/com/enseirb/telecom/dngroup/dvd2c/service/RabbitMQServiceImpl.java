package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Collections;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

import com.enseirb.telecom.dngroup.dvd2c.WorkerMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

@Service
public class RabbitMQServiceImpl implements MessageBrokerService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RabbitMQServiceImpl.class);

	public static final String RESULTQUEUE = "transcode-result";

	@Inject
	ContentService contentService;

	@Inject
	volatile Channel channel;

	@Inject
	volatile RabbitAdmin admin;

	@Inject
	volatile ConnectionFactory conFact;

	@Inject
	QueueConsumerAppImp queueConsumerApp;

	@Inject
	ObjectMapper mapper;

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
	public void addTask(String task, String id)
			throws UnsupportedEncodingException, IOException {

		channel.basicPublish("", QUEUE_NAME, new AMQP.BasicProperties.Builder()
				.contentType("application/json").contentEncoding("utf-8")
				.build(), task.getBytes("utf-8"));

		try {
			queueConsumerApp.getQueueMessage(id, contentService);
		} catch (InterruptedException e) {
			LOGGER.error("Queue interrupted", e);
		}
	}

	@Override
	public void setupResultQueue() {

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(conFact);
		container.setQueueNames(RESULTQUEUE);

		container.setMessageListener(new ChannelAwareMessageListener() {

			SimpleMessageConverter conv = new SimpleMessageConverter();

			@Override
			public void onMessage(Message message, Channel channel)
					throws Exception {

				WorkerMessage wm = mapper.readValue(
						(byte[]) conv.fromMessage(message), WorkerMessage.class);

				System.out.println(wm);

			}
		});

		// declaring the queue if it's not already present
		admin.declareQueue(new org.springframework.amqp.core.Queue(RESULTQUEUE,
				true, false, false, Collections.EMPTY_MAP));

		container.setErrorHandler(new ErrorHandler() {

			@Override
			public void handleError(Throwable t) {
				LOGGER.warn("error received while using rabbitmq container, trying to redeclare the queue");
				admin.declareQueue(new org.springframework.amqp.core.Queue(
						RESULTQUEUE, true, false, false, Collections.EMPTY_MAP));

			}
		});
		container.start();
	}
}
