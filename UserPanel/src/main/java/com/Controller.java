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
    private boolean _locked;
    private StatusUP _status;
    private DateTimeFormatter _formatter;

    public Controller(Model m, View v, MonitorView monitorView, ConsumerTopic consumer, RequestTable requestTable)
    {
        this._model = m;
        this._view = v;
        this._monitorView = monitorView;
        this._consumer = consumer;
        _pinTable = requestTable;
        this._status = new StatusUP();
        this._formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this._status.setGranted("N/A"); 
        this._status.setBlocked("N/A"); 
        this._status.setUser("N/A");
    }

    public void enterCommand()
    {
        Integer val = _model.checkPass();
        Integer key = _pinTable.addRecordNextKeyReturn(val);
        System.out.println("Getting request key");
        _consumer.askForAccess(key, val);
    }

    public void checkAccess()
    {
        if(!_consumer.getAccessStateSet())
        {
            System.out.println("Checking pin entry");
            _view.displayPassMessage("Loading...");
            if(_consumer.getAccessState() && _pinTable.doesKeyExist(_consumer.getId()))
            {
                _model.resetAttempts();
                LocalDateTime time = LocalDateTime.now();  
                // Format LocalDateTime
                this._status.setGranted(time.format(_formatter)); 
                this._status.setUser(_consumer.getUser()); 
                this._consumer.updateValues(_status);
                _view.displayPassMessage("Hello " + _consumer.getUser());
                this._view.close();
                this._monitorView.setMonitor();
            }
            else
            {
                if(_model.checkAttempts())
                {
                    LocalDateTime time = LocalDateTime.now();  
                    // Format LocalDateTime
                    this._status.setBlocked(time.format(_formatter));  
                    this._consumer.updateValues(_status);
                    _view.displayErrorMessage("Unsuccessful amount of attempts");
                    _locked = true;
                }
                else
                {
                    _view.displayErrorMessage("Wrong Passcode");
                }
            }
        }
        else
        {
            System.out.println("We didn't receive a access response");
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        String input = e.getActionCommand();
        checkAction(input);
    }

    private void killTime()
    {
        int j = 0;
        for(int i = 0; i < 99999; i++)
        {
            j = j + 1;
        }
        System.out.println("Killed waiting time");
    }

    public void checkAction(String input)
    {
        if(_locked == false)
        {
            if(Types.Actions.ENTER.name().equals(input))
            {
                System.out.println("User entered enter");
                if(_model.isValidPin())
                {
                    System.out.println("Pin is valid number, proceeding");
                    this.enterCommand();
                    this.killTime();
                    this.checkAccess();
                    _view.setDigits(_model.initModel(Types.EMPTY));
                }
                else
                {
                    System.out.println("User entered incorrect or empty pin");
                    _view.displayErrorMessage("Please enter a correct pin");
                }
            }
            else if(Types.Actions.ONE.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.ONE));
            }
            else if(Types.Actions.TWO.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.TWO));
            }
            else if(Types.Actions.THREE.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.THREE));
            }
            else if(Types.Actions.FOUR.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.FOUR));
            }
            else if(Types.Actions.FIVE.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.FIVE));
            }
            else if(Types.Actions.SIX.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.SIX));
            }
            else if(Types.Actions.SEVEN.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.SEVEN));
            }
            else if(Types.Actions.EIGHT.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.EIGHT));
            }
            else if(Types.Actions.NINE.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.NINE));
            }
            else if(Types.Actions.CLEAR.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.CLEAR));
            }
            else if(Types.Actions.ZERO.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.ZERO));
            }
            else if(Types.Actions.BACK.name().equals(input))
            {
                _view.setDigits(_model.setValue(Types.BACK));
            }
            else if(Types.State.OFF.name().equals(input))
            {
                _monitorView.setMonitorState(_model.setModelStateOFF());
                this.sendMonitorUpdate(false);
                this._status.setState(Types.OFF);
                this._consumer.updateValues(_status);
                _monitorView.close();
                _view.setView();
            }
            else if(Types.State.ON.name().equals(input))
            {
                _monitorView.setMonitorState(_model.setModelStateOn());
                this._status.setState(Types.ON);
                this._consumer.updateValues(_status);
                this.sendMonitorUpdate(true);
                _monitorView.close();
                _view.setView();
            }
        }
        else
        {
            _locked = _model.checkUnlock();
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
