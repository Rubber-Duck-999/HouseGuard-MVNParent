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
    private DateTimeFormatter _formatter;

    public Controller(Model m, View v, MonitorView monitorView, 
        ConsumerTopic consumer, RequestTable requestTable) {
        // Constructor
        this._model = m;
        this._view = v;
        this._monitorView = monitorView;
        this._consumer = consumer;
        _pinTable = requestTable;
        this._status = new StatusUP();
        this._formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public void enterCommand() {
        Integer val = _model.checkPass();
        Integer key = _pinTable.addRecordNextKeyReturn(val);
        System.out.println("Getting request key");
        _consumer.askForAccess(key, val);
    }

    public void checkAccess() {
        if(_consumer.getAccessState() && _pinTable.doesKeyExist(_consumer.getId())) {
            _model.resetAttempts();
            LocalDateTime time = LocalDateTime.now();  
            // Format LocalDateTime
            this._status.setGranted(time.format(_formatter)); 
            this._status.setUser(_consumer.getUser()); 
            this._consumer.updateValues(_status);
            _view.displayPassMessage("Hello " + _consumer.getUser());
            this._view.close();
            this._monitorView.setMonitor();
        } else {
            if(_model.checkAttempts()) {
                LocalDateTime time = LocalDateTime.now();  
                // Format LocalDateTime
                this._status.setBlocked(time.format(_formatter));  
                this._consumer.updateValues(_status);
                _view.displayErrorMessage("Unsuccessful amount of attempts");
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
                this._consumer.updateValues(_status);
                _view.setView();
                _monitorView.close();
                break;
            case Types.ON:
                _monitorView.setMonitorState(_model.setModelStateOn());
                this._status.setState(Types.ON);
                this._consumer.updateValues(_status);
                this.sendMonitorUpdate(true);
                _view.setView();
                _monitorView.close();
                break;
            default:
                _view.setDigits(_model.setValue(input));
                break;
        }

    }

    public void sendMonitorUpdate(boolean state)
    {
        _consumer.sendMonitorState(state);
    }

    public void initmodel(String x, String state)
    {
        this._status.setState(state);
        this._consumer.updateValues(_status);
        _view.setDigits(_model.initModel(x));
        _monitorView.setMonitorState(state);
    }
}
