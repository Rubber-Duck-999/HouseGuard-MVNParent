package com.house_guard.internal_system_controller;

public class Model 
{
    private RestAPI _rest;
    private Integer _counter;

    public void setConnection(String ip, String port, String guid) {
        _rest = new RestAPI(ip, port, guid);
        RequestData data = new RequestData(this._counter, "0", "0", "0");
        _rest.getData(data);
    }

    public void initModel() {
        this._counter = 1;
    }
    
}