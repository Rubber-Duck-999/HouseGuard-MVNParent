package com.house_guard.database_manager;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import com.house_guard.Common.*;
import com.google.gson.Gson;

public class TopicRabbitmq {
    private Integer _id;
    private String _routingKey;
    private String _message;
    private String _topicMessage;
    private LocalDateTime _timeSent;

    public TopicRabbitmq(String routingKey, String message) {
        _routingKey = routingKey;
        _message = message;
        this.convertMessage();
    }

    private void convertMessage() {
        Gson gson = new Gson();
        EventTopic eventData = gson.fromJson(_message, EventTopic.class);
        _topicMessage = eventData.getMessage();
        //
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(eventData.getTime(), dtf);
        _timeSent = dateTime;
    }

    public Integer getId()
    {
        return _id;
    }
    public void setId(Integer id)
    {
        this.id = _id;
    }

    public LocalDateTime getTimeSent() {
        return _timeSent;
    }

    public String getTopicMessage() {
        return _topicMessage;
    }

    public String getRoutingKey() {
        return _routingKey;
    }
}
