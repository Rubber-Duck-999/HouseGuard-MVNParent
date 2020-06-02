package com;

import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.awt.event.ActionEvent;
import com.house_guard.Common.*;
import java.util.logging.Logger;

public class Controller implements ActionListener
{
    public Model _model;
    public View _view;
    private MonitorView _monitorView;
    private ConsumerTopic _consumer;
    private RequestTable _pinTable;
    private StatusUP _status;

    public Controller(View v, MonitorView monitorView, 
        ConsumerTopic consumer, RequestTable requestTable) {
        // Constructor
        this._model = new Model();
        this._view = v;
        this._monitorView = monitorView;
        this._consumer = consumer;
        _pinTable = requestTable;
        this._status = new StatusUP();
    }

    public void enterCommand() {
        Integer val = _model.checkPass();
        Integer key = _pinTable.addRecordNextKeyReturn(val);
        System.out.println("Getting request key");
        _consumer.askForAccess(key, val);
    }

    private void stateUpdate(boolean state) {
        LocalDateTime time = LocalDateTime.now();  
        if(state) {
            this._status.setGranted(time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))); 
            _view.displayPassMessage("Hello " + _consumer.getUser());
        } else { 
            this._status.setBlocked(time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            _view.displayErrorMessage("Try again in 5 minutes");  
        }
        this._consumer.updateValues(_status);
    }

    public void checkAccess() {
        if(_consumer.getAccessState() && _pinTable.doesKeyExist(_consumer.getId())) {
            _model.resetAttempts();
            stateUpdate(true);
            this._view.close();
            this._monitorView.setMonitor();
        } else {
            if(_model.checkAttempts()) {
                stateUpdate(false);               
            } else {
                _view.displayErrorMessage("Wrong Passcode");
            }
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        String input = e.getActionCommand();
        checkAction(input);
    }

    private void killTime() {
        int j = 0;
        for(int i = 0; i < 99999; i++) {
            j = j + 1;
        }
        System.out.println("Killed waiting time");
    }

    private void Enter() {
        if(_model.isValidPin()) {
            System.out.println("Pin is valid number, proceeding");
            this.enterCommand();
            this.killTime();
            this.checkAccess();
            _view.setDigits(_model.initModel(Types.EMPTY));
        } else {
            System.out.println("User entered incorrect or empty pin");
        }
    }


    public void checkAction(String input) {
        if(_model.checkUnlock()) {
            return;
        }
        switch(input) {
            case Types.ENTER:
                System.out.println("User entered enter");
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
                this.sendMonitorUpdate(false);
                this._status.setState(Types.OFF);
                break;
            case Types.ON:
                _monitorView.setMonitorState(_model.setModelStateOn());
                this._status.setState(Types.ON);
                this.sendMonitorUpdate(true);
                break;
            default:
                _view.setDigits(_model.setValue(input));
                break;
        }

    }

    public void sendMonitorUpdate(boolean state)
    {
        _monitorView.setMonitorState(_model.setModelStateOn());
        this._status.setState(Types.ON);
        this._consumer.updateValues(_status);
        this.sendMonitorUpdate(state);
        _consumer.sendMonitorState(state);
        _view.setView();
        _monitorView.close();
    }

    public void initmodel(String x, String state)
    {
        this._status.setState(state);
        this._consumer.updateValues(_status);
        _view.setDigits(_model.initModel(x));
        _monitorView.setMonitorState(state);
    }
}
