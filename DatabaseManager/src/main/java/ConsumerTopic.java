package com.house_guard.database_manager;

import com.house_guard.Common.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.Vector;
import com.google.gson.Gson;


public class ConsumerTopic {
    private static final String kEXCHANGE_NAME = "topics";
    private static ConnectionFactory _factory;
    private static Connection _connection;
    private static Channel _channel;
    private String _subscribeQueueName;
    private Logger _LOGGER;
    private TopicsBuffer _buffer;
    private Gson gson;

    public boolean ConvertTopics(TopicRabbitmq topic, String message) {
        _LOGGER.info("Converting topics = " + topic.getRoutingKey());
        boolean type_found = true;
        if(topic.getRoutingKey().equals(Types.EVENT_TOPIC_UP)) {
            _LOGGER.info("Received a = " + topic.getRoutingKey());
        } else if(topic.getRoutingKey().equals(Types.EVENT_TOPIC_SYP)) {
            _LOGGER.info("Received a = " + topic.getRoutingKey());
        } else if(topic.getRoutingKey().equals(Types.EVENT_TOPIC_DBM)) {
            _LOGGER.info("Received a = " + topic.getRoutingKey());
        } else if(topic.getRoutingKey().equals(Types.EVENT_TOPIC_NAC)) {
            _LOGGER.info("Received a = " + topic.getRoutingKey());
        } else if(topic.getRoutingKey().equals(Types.EVENT_TOPIC_EVM)) {
            _LOGGER.info("Received a = " + topic.getRoutingKey());
        } else if(topic.getRoutingKey().equals(Types.EVENT_TOPIC_FH)) {
            _LOGGER.info("Received a = " + topic.getRoutingKey());
        } else if(topic.getRoutingKey().equals(Types.REQUEST_DATABASE_TOPIC)) {
            _LOGGER.info("Received a = " + topic.getRoutingKey());
            gson = new Gson();
            RequestDatabase data = gson.fromJson(message, RequestDatabase.class);
            Vector<DataInfoTopic> vector = _buffer.GetData(data);
            type_found = false;
        } else {
            type_found = false;
        }
        return type_found;
    }


    private void EventsTopicSubscribe(String routingKey, String message) {
        _LOGGER.info("Attempting to split message up by key");
        TopicRabbitmq local = new TopicRabbitmq(routingKey, message);
        if((this.ConvertTopics(local, message)) != false) {
            _LOGGER.info("Topic is a Event.* Topic");
            local.setValidTopic();
            if(local.convertMessage() == false) {
                _LOGGER.severe("We cannot convert this topic message");
            } else {
                _buffer.AddToList(local);
                _buffer.SortList();
            }
        }
    }

    public void ConsumeRequired() {
        try {
            _LOGGER.info("Setup of Queues and Exchange");
            _subscribeQueueName = _channel.queueDeclare().getQueue();
            //
            _channel.queueBind(_subscribeQueueName, kEXCHANGE_NAME, Types.EVENT_TOPIC_ALL);
            _channel.queueBind(_subscribeQueueName, kEXCHANGE_NAME, Types.REQUEST_DATABASE_TOPIC);
            //
            _LOGGER.info("Beginning consumption of topics, please ctrl+c to escape");
            //
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String received = new String(delivery.getBody());
                String key = delivery.getEnvelope().getRoutingKey();
                _LOGGER.info("Message received, key: " + key);
                this.EventsTopicSubscribe(key, received);
            };
            _channel.basicConsume(_subscribeQueueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
            _LOGGER.severe("Exception setting up consumption : " + e);
        }
    }


    public ConsumerTopic(TopicsBuffer buffer, Logger LOGGER) {
        _factory = new ConnectionFactory();
        _LOGGER = LOGGER;
        _factory.setHost("localhost");
        _factory.setPassword("password");
        _buffer = buffer;
        try {
            _connection = _factory.newConnection();
            _channel = _connection.createChannel();
            _channel.exchangeDeclare(kEXCHANGE_NAME, "topic", true);
        } catch (IOException | TimeoutException e) {
            _LOGGER.severe("We have had trouble setting up the required connection");
            e.printStackTrace();
        }
    }

}
