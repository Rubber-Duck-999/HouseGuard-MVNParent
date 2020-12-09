package com.house_guard.user_panel;

import com.house_guard.Common.*;
import java.time.LocalDateTime;

public class Model
{
    private final int MAX = 4;
    private String[] _digitArray;
    private int _currentDigit = 3;
    private int _attempts;
    private int _timeMinute;
    private boolean _lock;

    public Model()
    {
        _digitArray = new String[4];
        this._attempts = 0;
        _timeMinute = LocalDateTime.now().getMinute();
        _lock = false;
        for(int i = 0; i < MAX; i++)
        {
            _digitArray[i] = Types.ZERO;
        }
    }

    public String[] initModel(String x)
    {
        for(int i = 0; i < MAX; i++)
        {
            _digitArray[i] = x;
            _currentDigit = 3;
        }
        return _digitArray;
    }

    public String[] setValue(String value)
    {
        if(value.equals(Types.CLEAR))
        {
            initModel(Types.ZERO);
        }
        else
        {
            _digitArray[_currentDigit] = value;
            if(_currentDigit > 0)
            {
                _currentDigit--;
            }
        }
        return _digitArray;
    }

    public Integer checkPass()
    {
        Integer digits = 0;
        digits += Integer.parseInt(_digitArray[3]) * 1000;
        digits += Integer.parseInt(_digitArray[2]) * 100;
        digits += Integer.parseInt(_digitArray[1]) * 10;
        digits += Integer.parseInt(_digitArray[0]);
        return digits;
    }

    public boolean isValidPin()
    {
        for(int i = 0; i < MAX; i++)
        {
            if(_digitArray[i] == Types.EMPTY)
            {
                return false;
            }
        }
        return true;
    }

    public void resetAttempts()
    {
        this._attempts = 0;
    }

    public boolean checkAttempts()
    {
        this._attempts++;
        System.out.println("Atempts: " + this._attempts);
        if(this._attempts >= 3)
        {
            _timeMinute = LocalDateTime.now().getMinute();
            _lock = true;
            return _lock;
        } else {
            return _lock;
        }
    }

    public boolean checkUnlock()
    {
        int minute = LocalDateTime.now().getMinute();
        if((minute - _timeMinute) > 5)
        {
            _lock = false;
            resetAttempts();
            return _lock;
        }
        else
        {
            return _lock;
        }
    }

    public String setModelStateOn()
    {
        return Types.ON;
    }

    public String setModelStateOff()
    {
        return Types.OFF;
    }

    public void lock() 
    {
        _timeMinute = LocalDateTime.now().getMinute();
        _lock = true; 
	}
}
