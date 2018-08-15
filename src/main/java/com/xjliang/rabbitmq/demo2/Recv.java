package com.xjliang.rabbitmq.demo2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv {

	private static final String QUEUE_NAME="hello";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		//设置MabbitMQ所在主机ip或者主机名  
	    factory.setHost("192.168.9.22");
	    factory.setUsername("admin");
		factory.setPassword("123abc");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    //声明一个队列
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    boolean ack = false;
	    
	    final Consumer consumer = new DefaultConsumer(channel) {
	        @Override
	        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	            throws IOException {
	        	String message = new String(body, "UTF-8");

	            System.out.println(" [x] Received '" + message + "'");
	            try {
	              doWork(message);
	            } catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
	              System.out.println(" [x] Done");
	            }
	        }
	      };
	      channel.basicConsume(QUEUE_NAME, ack, consumer);
	      
	      
	}
	private static void doWork(String task) throws InterruptedException {
	    for (char ch: task.toCharArray()) {
	        if (ch == '.') Thread.sleep(1000);
	    }
	}
}
