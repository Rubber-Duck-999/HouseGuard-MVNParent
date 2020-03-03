package com.house_guard.database_manager;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import com.house_guard.Common.*;

public class DataInfoTopic extends Topic {
    private Integer _id;
    private String _routingKey;
    private String _topicMessage;
    private LocalDateTime _timeSent;

    public DataInfoTopic() {

    }

    public DataInfoTopic(String routingKey, String message, LocalDateTime time) {
        _routingKey = routingKey;
        _topicMessage = message;
        _timeSent = time;
    }

    public Integer getId()
    {
        return _id;
    }
    public void setId(Integer id)
    {
        this._id = id;
    }

    public LocalDateTime getTimeSent() {
        return _timeSent;
    }

    public void setTimeSent(LocalDateTime time) {
        _timeSent = time;
    }

    public String getTopicMessage() {
        return _topicMessage;
    }

    public void setTopicMessage(String message) {
        _topicMessage = message;
    }

    public String getRoutingKey() {
        return _routingKey;
    }

    public void setRoutingKey(String key) {
        _routingKey = key;
    }
}
