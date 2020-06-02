package com;

import com.house_guard.Common.*;
import java.time.LocalDateTime;

public class Model
{
    private final int MAX = 4;
    private String[] _digitArray;
    private int _currentDigit = 3;
    private int _attempts;
    private boolean _timeset;
    private int _timeDay;
    private int _timeHour;
    private int _timeMinute;
    private boolean _lock;

    public Model()
    {
        _digitArray = new String[4];
        _attempts = _timeDay = _timeHour = _timeMinute = 0;
        _lock = false;
        for(int i = 0; i < MAX; i++)
        {
            _digitArray[i] = Types.EMPTY;
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
        if(value.equals(Types.BACK))
        {
            if(_currentDigit == 0)
            {
                if(_digitArray[_currentDigit] == Types.EMPTY)
                {
                    _currentDigit++;
                }
                else
                {
                    _digitArray[_currentDigit] = Types.EMPTY;
                    _currentDigit++;
                    return _digitArray;
                }
            }
            else
            {
                if(_digitArray[_currentDigit] == Types.EMPTY)
                {
                    _currentDigit++;
                }
            }
            _digitArray[_currentDigit] = Types.EMPTY;
        }
        else if(value.equals(Types.CLEAR))
        {
            initModel(Types.EMPTY);
            _currentDigit = 3;
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
        _attempts = 0;
    }

    public boolean checkAttempts()
    {
        _attempts++;
        if(_attempts >= 3)
        {
            _timeDay = LocalDateTime.now().getDayOfMonth();
            _timeHour = LocalDateTime.now().getHour();
            _timeMinute = LocalDateTime.now().getMinute();
            _lock = true;
            return _lock;
        } else {
            return _lock;
        }
    }

    public boolean checkUnlock()
    {
        int day = LocalDateTime.now().getDayOfMonth();
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        System.out.println("Minute started: " + _timeMinute);
        System.out.println("Minute now: " + minute);
        if (hour >= _timeHour)
        {
            if((minute - _timeMinute) >= 5)
            {
                System.out.println("More than 5 minutes have occured, unlock");
                _lock = false;
                resetAttempts();
                return _lock;
            }
            else
            {
                System.out.println("5 minutes have not occured, lock continues");
                return _lock;
            }
        }
        return _lock;
    }

    public String setModelStateOn()
    {
        return Types.ON;
    }

    public String setModelStateOFF()
    {
        return Types.OFF;
    }
}
