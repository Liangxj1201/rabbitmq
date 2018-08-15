package com.xjliang.rabbitmq.demo2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Send {
	private static final String QUEUE_NAME="hello";
	
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.9.22");
		factory.setUsername("admin");
		factory.setPassword("123abc");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		/**
		 * 队列名，否持久化    rabbitMq 重启后队列不会丢失
		 */
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.queueDeclare("task_test1", true, false, false, null);
		String [] s = {"hello233..."};
		String message = getMessage(s);
		/**
		 * 设置公平调度，表示
		 * 再同一时刻，不要发送超过1条消息给一个工作者（worker），
		 * 直到它已经处理了上一条消息并且作出了响应。
		 * 这样，RabbitMQ就会把消息分发给下一个空闲的工作者（worker）。
		 */
		channel.basicQos(1);
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		/**
		 * 设置消息持久化，并不一定都持久化 也可能写入缓存，
		 * 要想完全持久化，需要改写你的代码来支持事务（transaction）
		 */
		channel.basicPublish("", "task_test1",  MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
	}
	private static String getMessage(String[] strings){
	    if (strings.length < 1)
	        return "Hello World!";
	    return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
	    int length = strings.length;
	    if (length == 0) return "";
	    StringBuilder words = new StringBuilder(strings[0]);
	    for (int i = 1; i < length; i++) {
	        words.append(delimiter).append(strings[i]);
	    }
	    return words.toString();
	}
}
