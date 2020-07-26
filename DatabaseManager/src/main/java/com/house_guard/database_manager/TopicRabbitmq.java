package com.house_guard.database_manager;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import com.house_guard.Common.*;
import com.google.gson.Gson;
import org.json.*;

public class TopicRabbitmq {
    private String _routingKey;
    private String _topicMessage;
    private LocalDateTime _timeOfReceival;
    private LocalDateTime _timeSent;
    private String _component;
    private String _eventTypeId;


    public TopicRabbitmq(String routingKey, String message) {
        _routingKey = routingKey;
        _topicMessage = message;
        this.setReceivalTime();
    }

    public boolean convertMessage() {
        Gson gson = new Gson();
        try {
            EventTopic eventData = gson.fromJson(_topicMessage, EventTopic.class);
            _topicMessage = eventData.getMessage();
            _timeSent = LocalDateTime.parse(eventData.getTime(), 
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
            System.out.println(_timeSent);
            //
            _component = eventData.getComponent();
            _eventTypeId = eventData.getEventTypeId();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setReceivalTime() {
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

    public String getTopicMessage() {
        return _topicMessage;
    }

    public String getRoutingKey() {
        return _routingKey;
    }

    public String getEventTypeId() {
        return _eventTypeId;
    }

}
