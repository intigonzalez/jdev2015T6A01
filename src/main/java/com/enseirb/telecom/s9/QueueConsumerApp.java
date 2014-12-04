package com.enseirb.telecom.s9;

import java.io.IOException;

import com.google.common.base.Throwables;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class QueueConsumerApp {

	  private final static String QUEUE_NAME = "celery";
	
	public static void main(String[] args) throws IOException, InterruptedException {

	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    com.rabbitmq.client.Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    final QueueingConsumer consumer = new QueueingConsumer(channel);
	    channel.basicConsume(QUEUE_NAME, true, consumer);

		new Thread(new Runnable() {

			@Override
			public void run() {
				/*
				try {
				    while (true) {
					      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
					      String message = new String(delivery.getBody());
					      System.out.println(" [x] Received '" + message + "'");
					    }
				} catch (ShutdownSignalException | ConsumerCancelledException | InterruptedException e) {
					throw Throwables.propagate(e);
				}*/
while(true){
			    QueueingConsumer.Delivery delivery = null;
				try {
					delivery = consumer.nextDelivery();
				} catch (ShutdownSignalException | ConsumerCancelledException
						| InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			    BasicProperties props = delivery.getProperties();
			    BasicProperties replyProps = new BasicProperties
			                                     .Builder()
			                                     .correlationId(props.getCorrelationId())
			                                     .build();

			    String message = new String(delivery.getBody());

			    System.out.println(" [.] " + message);
			}
			}
		}).start();
		
		Thread.currentThread().join();

		// httpServer.stop();
	}
}
