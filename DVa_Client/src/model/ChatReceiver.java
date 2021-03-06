package model;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Julio Savigny on 11/1/2016.
 */
public class ChatReceiver {

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    private Channel channel;
    private String exchangeName;
    private String routingKey;
    private Connection connection;
    private String queueName;
    private ArrayList<String> messages;

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public ChatReceiver(String host, String type, String key, String name) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            connection = factory.newConnection();
            channel = connection.createChannel();
            routingKey = key;
            exchangeName = name;

            if (type.equals("topic")) {
                channel.exchangeDeclare(exchangeName, "topic");
            } else if (type.equals("direct")) {
                channel.exchangeDeclare(exchangeName, "direct");
            }

            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, routingKey);



        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close() throws Exception {
        connection.close();
    }

}
