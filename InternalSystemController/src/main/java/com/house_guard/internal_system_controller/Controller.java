package com.house_guard.internal_system_controller;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;
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

    public Controller(Logger LOGGER, View v, Menu m) {
        _LOGGER = LOGGER;
        // Constructor
        this._model = new Model();
        this._view = v;
        this._menu = m;
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
                break;
            case "RESET":
                this._view.setPasswd("");
                break;
            case "SHOW":
                this._view.showPasswd();
                break;
            case "LOGS":
                this._model.getLogs("", "", "");
                //this._menu.changeView(false);
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
            default:
                _LOGGER.info("We should not have hit thus");
                break;
            }
    }
}
