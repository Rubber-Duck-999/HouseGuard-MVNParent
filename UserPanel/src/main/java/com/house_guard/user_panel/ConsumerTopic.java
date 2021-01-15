package com.house_guard.user_panel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.house_guard.Common.*;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class ConsumerTopic
{
    private static final String EXCHANGE_NAME = "topics";
    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;
    private Logger _LOGGER;

    private void publish(String json, String routingKey)
    {
        try
        {
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, json.getBytes());
        }
        catch (IOException e)
        {
            _LOGGER.info("We have had issues publishing");
            e.printStackTrace();
        }
    }

    public void sendMonitorState(boolean state)
    {
        Gson gson = new Gson();
        MonitorState mon = new MonitorState();
        mon.setState(state);
        String json = gson.toJson(mon);
        publish(json, Types.MONITOR_STATE_TOPIC);
    }

    public void publishStatus(StatusUP status)
    {
        _LOGGER.info("Publishing Status ");
        Gson gson = new Gson();
        String json = gson.toJson(status);
        publish(json, Types.STATUS_UP_TOPIC);
    }

    public void consumeRequired()
    {
        try
        {
            String subscribeQueueName = channel.queueDeclare().getQueue();
            //
            channel.queueBind(subscribeQueueName, EXCHANGE_NAME, Types.STATUS_REQUEST_UP_TOPIC);
            //
            _LOGGER.info(" [*] Waiting for TOPICS. To exit press CTRL+C");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String key = delivery.getEnvelope().getRoutingKey();
                _LOGGER.info("Message received: " + key);
                // this.publishStatus();
            };
            channel.basicConsume(subscribeQueueName, true, deliverCallback, consumerTag -> { });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setConnection(String password)
    {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPassword(password);
        try
        {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
        }
        catch (IOException | TimeoutException e)
        {
            _LOGGER.info("We have had trouble setting up the required connection");
            e.printStackTrace();
        }
    }

    public ConsumerTopic(Logger LOGGER)
    {
        _LOGGER = LOGGER;
    }
}
