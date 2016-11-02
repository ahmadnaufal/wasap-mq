package model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by Julio Savigny on 11/1/2016.
 */
public class ChatSender {
    private Connection connection;
    private Channel channel;
    private String exchangeName;
    private String routingKey;

    public ChatSender(String host, String type, String key, String name){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            connection = factory.newConnection();
            channel = connection.createChannel();
            routingKey = key;
            exchangeName = name;

            if (type == "topic") {
                channel.exchangeDeclare(exchangeName, "topic");
            } else if (type == "direct") {
                channel.exchangeDeclare(exchangeName, "direct");
            }

        } catch (Exception e){
            e.printStackTrace();
         }
    }

    public void send(String message) throws Exception {
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
    }

    public void close() throws Exception {
        connection.close();
    }
}
