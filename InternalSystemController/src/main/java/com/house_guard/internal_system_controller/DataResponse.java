package com.house_guard.internal_system_controller;

public class DataResponse {
    private int _id;
    private int _messageNum;
    private int _totalMessage;
    private String _topicMessage;
    private String _timeSent;

    public DataResponse() {

    }
    public DataResponse(int _id, int _messageNum, int _totalMessage, String _topicMessage, String _timeSent) {
        this._id = _id;
        this._messageNum = _messageNum;
        this._totalMessage = _totalMessage;
        this._topicMessage = _topicMessage;
        this._timeSent = _timeSent;
    }
    public int getId() {
        return this._id;
    }
    public void setId(int _id) {
        this._id = _id;
    }
    public int getMessageNum() {
        return this._messageNum;
    }
    public void setMessageNum(int _messageNum) {
        this._messageNum = _messageNum;
    }
    public int getTotalMessage() {
        return this._totalMessage;
    }
    public void setTotalMessage(int _totalMessage) {
        this._totalMessage = _totalMessage;
    }

    public String getTimeSent() {
        return this._timeSent;
    }

    public void setTimeSent(String _timeSent) {
        this._timeSent = _timeSent;
    }

    public String getTopicMessage() {
        return this._topicMessage;
    }

    public void setTopicMessage(String _topicMessage) {
        this._topicMessage = _topicMessage;
    }
}
