package com.house_guard.user_panel;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.awt.Color;

import com.house_guard.Common.AccessResponse;
import com.house_guard.Common.StatusUP;
import com.house_guard.Common.Types;

public class Controller implements ActionListener {
    public Model _model;
    public View _view;
    private MonitorView _monitorView;
    private ConsumerTopic _consumer;
    private Logger _LOGGER;
    private StatusUP _status;
    private DatabaseHelper _db;

    public Controller(Logger LOGGER, View v, MonitorView monitorView, 
        ConsumerTopic consumer, DatabaseHelper db) {
        _LOGGER = LOGGER;
        // Constructor
        this._model = new Model();
        this._view = v;
        this._db = db;
        this._monitorView = monitorView;
        this._consumer = consumer;
        this._status = new StatusUP();
    }

    private String getTime() {
        LocalDateTime time = LocalDateTime.now();
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private void stateUpdate(boolean state, String user) {
        if(state) {
            this._status.setGranted(getTime());
            this._consumer.publishStatus(this._status);
            _view.displayPassMessage("Hello " + user);
        } else { 
            this._status.setBlocked(getTime());
            this._consumer.publishStatus(this._status);
            _view.displayErrorMessage("Try again in 5 minutes");  
        }
    }

    public void checkAccess(AccessResponse access) {
        _LOGGER.info("checkAccess()");
        if(access.getResult() == Types.PASS) {
            _LOGGER.info("Correct pin");
            _model.resetAttempts();
            stateUpdate(true, access.getUser());
            this._view.close();
            this._monitorView.setMonitor();
        } else {
            if(_model.checkAttempts()) {
                _LOGGER.info("attempts reached");
                _model.lock();
                stateUpdate(false, "");       
            } else {
                _LOGGER.info("Incorrect passcode");
                _view.displayErrorMessage("Wrong Passcode");
                this._consumer.publishStatus(this._status);
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
                this.checkAccess(_db.checkUser(_model.checkPass()));
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
                _monitorView.setMonitorState(_model.setModelStateOff(), Color.RED);
                this.sendMonitorUpdate(false, Types.OFF);
                break;
            case Types.ON:
                _monitorView.setMonitorState(_model.setModelStateOn(), Color.GREEN);
                this.sendMonitorUpdate(true, Types.ON);
                break;
            case "HIDE":
                _monitorView.hide();
                try {
                    Thread.sleep(5000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                _monitorView.show();
                break;
            case "CHANGE":
                
                break;
            default:
                _view.setDigits(_model.setValue(input));
                break;
        }

    }

    public void sendMonitorUpdate(boolean state, String state_string) {
        _consumer.sendMonitorState(state);
        _view.setView();
        _monitorView.close();
    }

    private void switchAlarm(boolean val)
    {
        if(val) {
            _monitorView.setMonitorState(_model.setModelStateOn(), Color.GREEN);
        } else {
            _monitorView.setMonitorState(_model.setModelStateOff(), Color.RED);
        }
    }

    public void initmodel(String x, String state) {
        _view.setDigits(_model.initModel(x));
        this.switchAlarm(true);
    }
}
