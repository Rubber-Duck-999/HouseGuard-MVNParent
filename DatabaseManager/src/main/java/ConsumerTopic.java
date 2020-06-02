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
import java.util.Iterator;


public class ConsumerTopic {
    private static final String kEXCHANGE_NAME = "topics";
    private static ConnectionFactory _factory;
    private static Connection _connection;
    private static Channel _channel;
    private String _subscribeQueueName;
    private Logger _LOGGER;
    private TopicsBuffer _buffer;
    private Gson gson;

    public void PublishDataInfo(Vector<DataInfoTopic> vector) {
        gson = new Gson();
        Iterator i = vector.iterator();
        while (i.hasNext()) {
            _LOGGER.fine("Looping through publish list");
            String json = gson.toJson(i.next());
            PublishMessage(json, Types.DATA_INFO_TOPIC);
        }
    }

    private void PublishMessage(String json, String Topic) {
        _LOGGER.info("Message is : " + json);
        try {
            _channel.basicPublish(kEXCHANGE_NAME, Topic,
                                  null, json.getBytes());
        } catch(IOException e) {
            _LOGGER.info("We have had issues publishing");
            e.printStackTrace();
        } catch(NullPointerException e) {
            _LOGGER.warning("Issues with rabbitmq");
        }  
    }

    public void PublishDeviceResponse(DeviceResponse response) {
        gson = new Gson();
        String json = gson.toJson(response);
        PublishMessage(json, Types.DEVICE_RESPONSE_TOPIC);
    }

    public void PublishAccessResponse(AccessResponse response) {
        gson = new Gson();
        String json = gson.toJson(response);
        PublishMessage(json, Types.ACCESS_RESPONSE_TOPIC);
    }   

    public void PublishStatus() {
        gson = new Gson();
        String json = gson.toJson(_buffer.getStatus());
        PublishMessage(json, Types.STATUS_DBM_TOPIC);   
    }

    public boolean ConvertTopics(String routingKey, String message) {
        _LOGGER.info("Converting topics = " + routingKey);
        boolean type_found = false;
        gson = new Gson();
        switch(routingKey) {
            case Types.STATUS_REQUEST_DBM_TOPIC:
                PublishStatus();
                break;
            case Types.REQUEST_DATABASE_TOPIC:
                Vector<DataInfoTopic> vector = _buffer.GetEventData(gson.fromJson(message, RequestDatabase.class));
                _LOGGER.info("We have returned size of " + vector.size() + " data records.");
                PublishDataInfo(vector);
                break;
            case Types.DEVICE_REQUEST_TOPIC:
                PublishDeviceResponse(_buffer.GetDeviceData(gson.fromJson(message, DeviceRequest.class))); 
                break;              
            case Types.DEVICE_UPDATE_TOPIC:
                _buffer.CreateDevice(gson.fromJson(message, DeviceUpdate.class));
            case Types.REQUEST_ACCESS_TOPIC:
                PublishAccessResponse(_buffer.GetUser(gson.fromJson(message, RequestAccess.class)));
                break;
            default:
                type_found = true;
        }
        return type_found;
    }


    private void EventsTopicSubscribe(String routingKey, String message) {
        _LOGGER.info("Attempting to split message up by key");
        if((this.ConvertTopics(routingKey, message)) != false) {
            TopicRabbitmq local = new TopicRabbitmq(routingKey, message);
            _LOGGER.info("Topic is a Event.* Topic");
            local.setValidTopic();
            if(local.convertMessage() == false) {
                _LOGGER.severe("We cannot convert this topic message");
            } else {
                _buffer.AddTopic(local);
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
            _channel.queueBind(_subscribeQueueName, kEXCHANGE_NAME, Types.DEVICE_UPDATE_TOPIC);
            _channel.queueBind(_subscribeQueueName, kEXCHANGE_NAME, Types.DEVICE_REQUEST_TOPIC);
            _channel.queueBind(_subscribeQueueName, kEXCHANGE_NAME, Types.REQUEST_ACCESS_TOPIC);
            _channel.queueBind(_subscribeQueueName, kEXCHANGE_NAME, Types.STATUS_REQUEST_DBM_TOPIC);
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


    public ConsumerTopic(String password, TopicsBuffer buffer, Logger LOGGER) {
        _factory = new ConnectionFactory();
        _LOGGER = LOGGER;
        _factory.setHost("localhost");
        _factory.setPassword(password);
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
