package com.house_guard.user_panel;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.awt.Color;

import com.house_guard.Common.Types;

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
            this._consumer.publishStatus();
            _view.displayErrorMessage("Try again in 5 minutes");  
        }
    }

    public void checkAccess() {
        _LOGGER.info("checkAccess()");
        if(!_consumer.getAccessRequested() && _consumer.getAccessState()) {
            _LOGGER.info("Correct pin");
            if(_pinTable.doesKeyExist(_consumer.getId())) {
                _model.resetAttempts();
                stateUpdate(true);
                this._view.close();
                this._monitorView.setMonitor();
            } else {
                _LOGGER.info("Ids for pin request and response do not match");
            }
        } else {
            if(_model.checkAttempts()) {
                _LOGGER.info("attempts reached");
                _model.lock();
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
        try {
            if(_model.isValidPin()) {
                _LOGGER.info("Pin is a valid number, proceeding");
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
        if(_model.checkUnlock()) {
            return;
        }
        switch(input) {
            case Types.ENTER:
                this.Enter();
                break;
            case Types.CLEAR:
                _view.setDigits(_model.setValue(Types.CLEAR));
                break;
            case Types.BACK:
                _view.displayErrorMessage("This feature is currently disabled");
                break;
            case Types.OFF:
                _monitorView.setMonitorState(_model.setModelStateOFF(), Color.RED);
                this.sendMonitorUpdate(false, Types.OFF);
                break;
            case Types.ON:
                _monitorView.setMonitorState(_model.setModelStateOn(), Color.GREEN);
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
        _monitorView.setMonitorState(state, Color.GREEN);
    }
}
