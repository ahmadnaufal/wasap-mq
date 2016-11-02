package model;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Created by Julio Savigny on 11/1/2016.
 * DVa : ONE, bad guys : ZERO
 * DVa ~~ Rabbit
 */
public class DVaRPCClient {
    private Connection connection;
    private Channel channel;
    private String requestQueueName;
    private String replyQueueName;
    private QueueingConsumer consumer;

    public DVaRPCClient(String queueName) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        requestQueueName = queueName;

        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public JSONObject call(JSONObject message) throws Exception {
        JSONObject response = null;
        String corrId = java.util.UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        System.out.println(message.toJSONString());
        channel.basicPublish("", requestQueueName, props, message.toJSONString().getBytes());

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                JSONParser parser = new JSONParser();
                String resmessage = new String(delivery.getBody());
                response = (JSONObject) parser.parse(resmessage);
                break;
            }
        }

        return response;
    }

    public void close() throws Exception {
        connection.close();
    }
}