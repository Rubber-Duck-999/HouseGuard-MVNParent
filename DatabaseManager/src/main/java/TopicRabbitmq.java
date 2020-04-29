package com.house_guard.database_manager;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import com.house_guard.Common.*;
import com.google.gson.Gson;
import org.json.*;

public class TopicRabbitmq {
    private boolean _validTopic;
    private String _routingKey;
    private String _message;
    private String _topicMessage;
    private LocalDateTime _timeOfReceival;
    private LocalDateTime _timeSent;
    private String _component;


    public TopicRabbitmq(String routingKey, String message) {
        _routingKey = routingKey;
        _message = message;
        this.setReceivalTime();
        _validTopic = false;
    }

    public boolean convertMessage() {
        Gson gson = new Gson();
        try {
            EventTopic eventData = gson.fromJson(_message, EventTopic.class);
            _topicMessage = eventData.getMessage();
            //
            System.out.println("Event data: " + eventData.getTime());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(eventData.getTime(), dtf);
            _timeSent = dateTime;
            //
            _component = eventData.getComponent();
            return true;
        } catch(Exception e) {
            System.out.println("We have had trouble converting: " + e);
            e.printStackTrace();
            return false;
        }
    }

    private void setReceivalTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        _timeOfReceival = LocalDateTime.now();
    }

    public LocalDateTime getTimeReceived() {
        return _timeOfReceival;
    }

    public LocalDateTime getTimeSent() {
        return _timeSent;
    }

    public String getComponent() {
        return _component;
    }

    public boolean getValidity() {
        return _validTopic;
    }

    public void setValidTopic() {
        _validTopic = true;
    }

    public String getTopicMessage() {
        return _topicMessage;
    }

    public String getRoutingKey() {
        return _routingKey;
    }
}
