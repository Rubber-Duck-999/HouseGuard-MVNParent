package com;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import com.house_guard.Common.*;


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
    private String subscribeQueueName;
    private boolean accessAllowed;
    private boolean accessRequested;
    private Integer receivedId;
    private Gson gson;
    private StatusUP status;
    private String user;

    public boolean getAccessState()
    {
        return accessAllowed;
    }

    public String getUser()
    {
        return user;
    }

    public boolean getAccessRequested()
    {
        return accessRequested;
    }

    public void setAccessRequestedOff()
    {
        accessRequested = false;
    }

    public void updateValues(StatusUP up)
    {
        this.status = up;
    }

    public void sendMonitorState(boolean state)
    {
        String routingKey = Types.MONITOR_STATE_TOPIC;
        gson = new Gson();
        MonitorState mon = new MonitorState();
        mon.setState(state);
        String json = gson.toJson(mon);
        System.out.println("Message is : " + json);
        try
        {
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, json.getBytes());
        }
        catch (IOException e)
        {
            System.out.println("We have had issues publishing");
            e.printStackTrace();
        }
    }

    public void askForAccess(Integer key, Integer val)
    {
        System.out.println("Requesting access ");
        String routingKey = Types.REQUEST_ACCESS_TOPIC;
        gson = new Gson();
        RequestAccess req = new RequestAccess();
        req.setId(key);
        req.setPin(val);
        String json = gson.toJson(req);
        accessRequested = false;
        accessAllowed = false;
        try
        {
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, json.getBytes());
        }
        catch (IOException e)
        {
            System.out.println("We have had issues publishing");
            e.printStackTrace();
        }
    }

    public void publishStatus()
    {
        System.out.println("Publishing Status ");
        gson = new Gson();
        String json = gson.toJson(this.status);
        System.out.println(json);
        try
        {
            channel.basicPublish(EXCHANGE_NAME, Types.STATUS_UP_TOPIC, null, json.getBytes());
        }
        catch (IOException e)
        {
            System.out.println("We have had issues publishing");
            e.printStackTrace();
        }
    }

    private void accessResponse(String delivery, String routingKey)
    {
        System.out.println("Received response");
        System.out.println(delivery);
        accessRequested = true;
        AccessResponse data = gson.fromJson(delivery, AccessResponse.class);
        receivedId = data.getId();
        if(!data.getResult().equals(Types.PASS))
        {
            System.out.println("Publishing event.Up topic");
            accessAllowed = false;
            String pubMessage = createEventUpMessage();
            try
            {
                channel.basicPublish(EXCHANGE_NAME, Types.EVENT_TOPIC_UP, null, pubMessage.getBytes());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(data.getResult().equals(Types.PASS))
        {
            accessAllowed = true;
            user = data.getUser();
        }
    }

    private String createEventUpMessage()
    {
        EventTopic user_event = new EventTopic();
        user_event.setComponent(Types.COMPONENT_NAME);
        user_event.setMessage(Types.RequestFailure);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date();
        user_event.setTime(formatter.format(date));
        user_event.setEventTypeId("UP2");
        String pubMessage = gson.toJson(user_event);
        return pubMessage;
    }

    public void consumeRequired()
    {
        try
        {
            Gson gson = new Gson();
            subscribeQueueName = channel.queueDeclare().getQueue();
            //
            channel.queueBind(subscribeQueueName, EXCHANGE_NAME, Types.ACCESS_RESPONSE_TOPIC);
            channel.queueBind(subscribeQueueName, EXCHANGE_NAME, Types.STATUS_REQUEST_UP_TOPIC);
            //
            System.out.println(" [*] Waiting for access.response. To exit press CTRL+C");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                System.out.println("Message received");
                String received = new String(delivery.getBody());
                String key = delivery.getEnvelope().getRoutingKey();
                if(key.equals(Types.ACCESS_RESPONSE_TOPIC)) {
                    this.accessResponse(received, key);
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
            System.out.println("We have had trouble setting up the required connection");
            e.printStackTrace();
        }
    }


    public ConsumerTopic()
    {
        receivedId = 0;
        accessAllowed = false;
        accessRequested = false;
    }

    public Integer getId()
    {
        return receivedId;
    }
}
