package com.house_guard.internal_system_controller;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Logger;

public class Controller implements ActionListener {
    public Model _model;
    public View _view;
    public Menu _menu;
    public Users _users;
    public Logs _logs;
    public Devices _devices;
    public Settings _settings;
    private Logger _LOGGER;

    public Controller(Logger LOGGER, View v, Menu m, Logs l) {
        _LOGGER = LOGGER;
        // Constructor
        this._model = new Model(_LOGGER);
        this._view = v;
        this._menu = m;
        this._logs = l;
    }


    private void stateUpdate(boolean state) {
        LocalDateTime time = LocalDateTime.now();  
        if(state) {
            this._view.displayPassMessage("Hello ");//_consumer.getUser());
        } else { 
            this._view.displayErrorMessage("Try again in 5 minutes");  
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        _LOGGER.warning("Checking Action");
        switch(e.getActionCommand()) {
            case "LOGIN":
                this.getLogin();
                break;
            case "RESET":
                this._view.setPasswd("");
                break;
            case "SHOW":
                this._view.showPasswd();
                break;
            case "LOGS":
                this._logs.changeView(true);
                this._menu.changeView(false);
                break;
            case "USERS":
                this._menu.changeView(false);
                break;
            case "DEVICES":
                this._menu.changeView(false);
                break;
            case "SETTINGS":
                this._menu.changeView(false);
                break;
            case "LOGS - CREATE":
                getLogs();
                break;
            default:
                _LOGGER.info("We should not have hit this");
                break;
            }
    }

    private void getLogin() {
        _LOGGER.info("Checking login details");
        this._model.setConnection(this._view.getIpText(), 
        this._view.getPortText(), 
        this._view.getPasswd());
        if (this._model.correctCredentials()) {
            this._view.displayPassMessage("Login Successful");
            this._view.changeLogin(false);
            this._menu.changeView(true);
        } else {
            this._view.displayErrorMessage("Invalid Username or Password");
        }
    }

    private void getLogs() {
        LocalTime timeFrom = this._logs.getTimeFrom();
        LocalTime timeTo   = this._logs.getTimeTo();
        String event = convertEvent(this._logs.getEventType());
        _LOGGER.info("Time: " + timeFrom + ", Time: " + timeTo);
        if(timeFrom.isBefore(timeTo)) {
            _LOGGER.info("Time range is suitable");
            String[][] array = this._model.getLogs(timeFrom.toString(), timeTo.toString(), event);
            this._logs.displayPassMessage("Retrieving Logs, please wait...");
            System.out.println("Array values length: " + array.length);
            Graph _graph = new Graph(array);
        } else {
            _LOGGER.warning("Time range was invalid");
        }
    }

    private String convertEvent(String eventType) {
        String choice;
        switch(eventType) {
            case Constants.kEmailServer:
                choice = Constants.kEmail;
                break;
            case Constants.kMotionDetected:
                choice = Constants.kMotion;
                break;
            default:
                choice = "";
                break;
        }
        return choice;
    }
}
