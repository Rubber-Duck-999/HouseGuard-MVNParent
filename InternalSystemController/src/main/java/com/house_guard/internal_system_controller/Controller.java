package com.house_guard.internal_system_controller;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class Controller implements ActionListener
{
    //public Model _model;
    public View _view;
    private Logger _LOGGER;

    public Controller(Logger LOGGER, View v) {
        _LOGGER = LOGGER;
        // Constructor
        //this._model = new Model();
        this._view = v;
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
        String input = e.getActionCommand();
        //checkAction(input);
        //Coding Part of LOGIN button
        if (input.contentEquals("LOGIN")) {
            if (this._view.getUserText().equalsIgnoreCase("m") 
                && 
                this._view.getPasswd().equalsIgnoreCase("1")) {
                this._view.displayPassMessage("Login Successful");
            } else {
                this._view.displayErrorMessage("Invalid Username or Password");
            }
    
        }
        //Coding Part of RESET button
        if (input.contentEquals("RESET")) {
            this._view.setPasswd("");
        }
        //Coding Part of showPassword JCheckBox
        if (input.contentEquals("SHOW")) {
            this._view.showPasswd();
        }
    }


    public void checkAction(String input) {
        switch(input) {
            case "LOGIN":
                //this.Enter();
                break;
            case "RESET":
                //_view.setDigits(_model.setValue(Types.CLEAR));
                break;
            default:
                //_view.setDigits(_model.setValue(input));
                break;
        }

    }
}
