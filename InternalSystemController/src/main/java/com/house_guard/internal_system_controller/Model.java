package com.house_guard.internal_system_controller;

public class Model 
{
    private RestAPI _rest;
    private boolean _correct;

    public void setConnection(String ip, String port, String guid) {
        _rest = new RestAPI(ip, port, guid);
        RequestData data = new RequestData("Test", "0", "0");
        if (_rest.testData(data)) {
            _correct = true;
        } else {
            _correct = false;
        }
    }

    public void getLogs(String time_from, String time_to, String event_type) {
        RequestData data = new RequestData(event_type, time_from, time_to);
        _rest.getLogs(data);
    }

    public boolean correctCredentials() {
        return _correct;
    }

    public Model() {

    }
    
}