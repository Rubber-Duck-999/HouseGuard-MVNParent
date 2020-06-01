package com.house_guard.database_manager;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import com.house_guard.Common.*;

public class DataInfoTopic extends Topic {
    private Integer _id;
    private String _routingKey;
    private int _messageNum;
    private int _totalMessage;
    private String _topicMessage;
    private String _timeSent;

    public DataInfoTopic() {

    }

    public Integer getId()
    {
        return _id;
    }
    public void setId(Integer id)
    {
        this._id = id;
    }
    public int getMessageNum()
    {
        return _messageNum;
    }
    public void setMessageNum(int num)
    {
        this._messageNum = num;
    }
    public int getTotalMessage()
    {
        return _totalMessage;
    }
    public void setTotalMessage(int total)
    {
        this._totalMessage = total;
    }

    public String getTimeSent() {
        return _timeSent;
    }

    public void setTimeSent(String time) {
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
