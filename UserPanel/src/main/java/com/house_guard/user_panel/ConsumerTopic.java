package com.house_guard.user_panel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private boolean accessAllowed;
    private boolean accessRequested;
    private Integer receivedId;
    private StatusUP status;
    private Logger _LOGGER;
    private boolean stateUpdated;
    private boolean alarmState;

    public void setGranted(String granted)
    {
        this.status.setGranted(granted);
    }

    public void setBlocked(String granted)
    {
        this.status.setBlocked(granted);
    }

    public void setState(String state)
    {
        this.status.setState(state);
    }

    public boolean getAlarmState()
    {
        return this.alarmState;
    }

    public void setStateUpdated(boolean val)
    {
        this.stateUpdated = val;
    }

    public boolean isStateUpdated()
    {
        _LOGGER.info("State is updated: " + this.stateUpdated);
        return this.stateUpdated;
    }

    public boolean getAccessState()
    {
        return accessAllowed;
    }

    public boolean getAccessRequested()
    {
        return accessRequested;
    }

    public void setAccessRequestedOff()
    {
        accessRequested = false;
    }

    public String getUser()
    {
        return this.status.getUser();
    }

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
        this.publishStatus();
        Gson gson = new Gson();
        MonitorState mon = new MonitorState();
        mon.setState(state);
        if(!state) 
        {
            publishEventUP("UP1");
        }
        String json = gson.toJson(mon);
        publish(json, Types.MONITOR_STATE_TOPIC);
    }

    public void askForAccess(Integer key, Integer val)
    {
        _LOGGER.info("Requesting access ");
        Gson gson = new Gson();
        RequestAccess req = new RequestAccess(key, val);
        String json = gson.toJson(req);
        accessRequested = true;
        accessAllowed = false;
        publish(json, Types.REQUEST_ACCESS_TOPIC);
    }

    public void publishStatus()
    {
        _LOGGER.info("Publishing Status ");
        Gson gson = new Gson();
        String json = gson.toJson(this.status);
        publish(json, Types.STATUS_UP_TOPIC);
    }

    private void accessResponse(String delivery, String routingKey)
    {
        accessRequested = false;
        _LOGGER.info("Received response");
        Gson gson = new Gson();
        AccessResponse data = gson.fromJson(delivery, AccessResponse.class);
        receivedId = data.getId();
        if(!data.getResult().equals(Types.PASS))
        {
            _LOGGER.info("Publishing event.Up topic");
            accessAllowed = false;
        }
        else if(data.getResult().equals(Types.PASS))
        {
            accessAllowed = true;
            this.status.setUser(data.getUser());
        }
    }

    public void alarmUpdate(boolean update)
    {
        _LOGGER.info("Updating alarm to db");
        Gson gson = new Gson();
        AlarmResponse alarm = new AlarmResponse();
        alarm.setState(update);
        String json = gson.toJson(alarm);
        publish(json, Types.ALARM_UPDATE_TOPIC);
    }


    private void updateAlarmResponse(String received)
    {
        _LOGGER.info("Received alarm response");
        Gson gson = new Gson();
        AlarmResponse data = gson.fromJson(received, AlarmResponse.class);
        _LOGGER.info("Changing state from: " + this.alarmState + " to " + data.getState());
        this.alarmState = data.getState();
        this.setStateUpdated(true);
    }

    public void requestAlarm()
    {
        _LOGGER.info("Request alarm request topic");
        Gson gson = new Gson();
        AlarmResponse alarm = new AlarmResponse();
        alarm.setState(false);
        String json = gson.toJson(alarm);
        publish(json, Types.ALARM_REQUEST_TOPIC);
    }


    public void publishEventUP(String event_type_id)
    {
        EventTopic user_event = new EventTopic();
        user_event.setComponent(Types.COMPONENT_NAME);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date();
        user_event.setTime(formatter.format(date));
        user_event.setEventTypeId(event_type_id);
        Gson gson = new Gson();
        String pubMessage = gson.toJson(user_event);
        try
        {
            channel.basicPublish(EXCHANGE_NAME, Types.EVENT_TOPIC_UP, null, pubMessage.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void consumeRequired()
    {
        try
        {
            String subscribeQueueName = channel.queueDeclare().getQueue();
            //
            channel.queueBind(subscribeQueueName, EXCHANGE_NAME, Types.ACCESS_RESPONSE_TOPIC);
            channel.queueBind(subscribeQueueName, EXCHANGE_NAME, Types.STATUS_REQUEST_UP_TOPIC);
            channel.queueBind(subscribeQueueName, EXCHANGE_NAME, Types.ALARM_RESPONSE_TOPIC);
            //
            _LOGGER.info(" [*] Waiting for TOPICS. To exit press CTRL+C");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String received = new String(delivery.getBody());
                String key = delivery.getEnvelope().getRoutingKey();
                _LOGGER.info("Message received: " + key);
                if(key.equals(Types.ACCESS_RESPONSE_TOPIC)) {
                    this.accessResponse(received, key);
                } else if(key.equals(Types.ALARM_RESPONSE_TOPIC)) {
                    this.updateAlarmResponse(received);
                } else {
                    this.publishStatus();
                }
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
        receivedId = 0;
        accessAllowed = false;
        accessRequested = false;
        stateUpdated = false;
        alarmState = false;
        this.status = new StatusUP();
    }

    public Integer getId()
    {
        return receivedId;
    }
}
