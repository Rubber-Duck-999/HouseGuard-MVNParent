package com.house_guard.internal_system_controller;

import java.util.logging.Logger;

public class Model 
{
    private RestAPI _rest;
    private boolean _correct;
    private Logger _logger;

    public void setConnection(String ip, String port, String guid) {
        _logger.info("Setting rest connection up");
        _rest = new RestAPI(_logger, ip, port, guid);
        RequestData data = new RequestData("Test", "0", "0");
        if (_rest.testData(data)) {
            _correct = true;
        } else {
            _correct = false;
        }
    }

    public void getLogs(String time_from, String time_to, String event_type) {
        _logger.info("Requesting logs");
        RequestData data = new RequestData(event_type, time_from, time_to);
        _rest.getLogs(data);
    }

    public boolean correctCredentials() {
        return _correct;
    }

    public Model(Logger logger) {
        this._logger = logger;
    }
    
}