package com.enseirb.telecom.dngroup.dvd2c;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.RabbitMQServer;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class QueueConsumerApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueueConsumerApp.class);
	/**
	 * Create a queue with the correlation_id of the task, as we can wait the end message
	 * @param queue
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void getQueueMessage(String queue) throws IOException,
			InterruptedException {
		final String QUEUE_NAME = queue;

		ConnectionFactory factory = new ConnectionFactory();
		
		factory.setHost(CliConfSingleton.rabbitHostname);
		factory.setPort(CliConfSingleton.rabbitPort );
		LOGGER.debug("Rabbit conection : host : {} , port : {} ",factory.getHost(),factory.getPort());
		com.rabbitmq.client.Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
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
				
				ContentService contentService = new ContentServiceImpl(
						new ContentRepositoryMongo(), null);

			
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
