package com;

import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;
import com.house_guard.Common.*;
import java.util.logging.Logger;

public class Controller implements ActionListener
{
    public Model _model;
    public View _view;
    private MonitorView _monitorView;
    private ConsumerTopic _consumer;
    private RequestTable _pinTable;
    private Logger _LOGGER;

    public Controller(Logger LOGGER, View v, MonitorView monitorView, 
        ConsumerTopic consumer, RequestTable requestTable) {
        _LOGGER = LOGGER;
        // Constructor
        this._model = new Model();
        this._view = v;
        this._monitorView = monitorView;
        this._consumer = consumer;
        _pinTable = requestTable;
    }

    public void enterCommand() {
        Integer val = _model.checkPass();
        Integer key = _pinTable.addRecordNextKeyReturn(val);
        _LOGGER.info("Getting request key");
        _consumer.askForAccess(key, val);
    }

    private void stateUpdate(boolean state) {
        LocalDateTime time = LocalDateTime.now();  
        if(state) {
            this._consumer.setGranted(time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))); 
            _view.displayPassMessage("Hello " + _consumer.getUser());
        } else { 
            this._consumer.setBlocked(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            _view.displayErrorMessage("Try again in 1 minute");  
        }
    }

    public void checkAccess() {
        _LOGGER.info("checkAccess()");
        if(_consumer.getAccessState() && _pinTable.doesKeyExist(_consumer.getId())) {
            _model.resetAttempts();
            stateUpdate(true);
            this._view.close();
            this._monitorView.setMonitor();
        } else {
            if(_model.checkAttempts()) {
                _LOGGER.info("attempts reached");
                stateUpdate(false);               
            } else {
                _LOGGER.info("Incorrect passcode");
                _view.displayErrorMessage("Wrong Passcode");
            }
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        String input = e.getActionCommand();
        checkAction(input);
    }

    private void Enter() {
        if(_model.checkUnlock()) {
            return;
        }
        try {
            if(_model.isValidPin()) {
                _LOGGER.info("Pin is valid number, proceeding");
                this.enterCommand();
                TimeUnit.SECONDS.sleep(1);
                this.checkAccess();
                _view.setDigits(_model.initModel(Types.ZERO));
            } else {
                _LOGGER.info("User entered incorrect or empty pin");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void checkAction(String input) {
        switch(input) {
            case Types.ENTER:
                this.Enter();
                break;
            case Types.CLEAR:
                _view.setDigits(_model.setValue(Types.CLEAR));
                break;
            case Types.BACK:
                _view.setDigits(_model.setValue(Types.BACK));
                break;
            case Types.OFF:
                _monitorView.setMonitorState(_model.setModelStateOFF());
                this.sendMonitorUpdate(false, Types.OFF);
                break;
            case Types.ON:
                _monitorView.setMonitorState(_model.setModelStateOn());
                this.sendMonitorUpdate(true, Types.ON);
                break;
            default:
                _view.setDigits(_model.setValue(input));
                break;
        }

    }

    public void sendMonitorUpdate(boolean state, String state_string)
    {
        this._consumer.setState(state_string);
        _consumer.sendMonitorState(state);
        _view.setView();
        _monitorView.close();
    }

    public void initmodel(String x, String state)
    {
        this._consumer.setState(state);
        _view.setDigits(_model.initModel(x));
        _monitorView.setMonitorState(state);
    }
}
