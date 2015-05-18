package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Service
public class QueueConsumerAppImp implements QueueConsumerApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueueConsumerAppImp.class);
	
	@Inject
	ConnectionFactory factory;
	
	/* (non-Javadoc)
	 * @see com.enseirb.telecom.dngroup.dvd2c.QueueConumerApp#getQueueMessage(java.lang.String, com.enseirb.telecom.dngroup.dvd2c.service.ContentService)
	 */
	@Override
	public void getQueueMessage(String queue,final ContentService contentService) throws IOException,
			InterruptedException {
		final String QUEUE_NAME = queue;

		
		LOGGER.debug("Rabbit conection : host : {} , port : {} ",factory.getHost(),factory.getPort());
		Connection connection = factory.createConnection();
		Channel channel = connection.createChannel(false);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("x-expires", 86400000);
		channel.queueDeclare(QUEUE_NAME, true, false, true, map);
		
		LOGGER.info("Queue Name : {}", QUEUE_NAME);
		LOGGER.info(" [*] Waiting for messages from RabbitMQ.");

		channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					BasicProperties properties, byte[] body) throws IOException {
				String result = new String(body);
				JSONObject obj;
				String status = null;
				
				
				try {
					obj = new JSONObject(result);
					status = obj.getString("status");
					LOGGER.info("Result for element {}" , new Object[] {result, status});
				} catch (JSONException e) {
					LOGGER.error("error for pars element",e);
				}

			
				if (status.equals("SUCCESS")) {
					// change the status in DataBase
					LOGGER.info("Response from Celery : Success for task ", QUEUE_NAME);
					contentService.updateContent(QUEUE_NAME, "success");
				}
				else {
					contentService.updateContent(QUEUE_NAME, "failure");
					LOGGER.info("Response from Celery : Failure for task ", QUEUE_NAME);
				}
			}
		});

	}
}
