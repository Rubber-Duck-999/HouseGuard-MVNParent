package com.house_guard.internal_system_controller;

public class DataResponse {
    private int _id;
    private int _messageNum;
    private int _totalMessage;
    private String _topicMessage;
    private String _timeSent;

    public DataResponse() {

    }

    public int getId() {
        return _id;
    }
    public void setId(int id) {
        this._id = id;
    }
    public int getMessageNum() {
        return _messageNum;
    }
    public void setMessageNum(int num) {
        this._messageNum = num;
    }
    public int getTotalMessage() {
        return _totalMessage;
    }
    public void setTotalMessage(int total) {
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
}
