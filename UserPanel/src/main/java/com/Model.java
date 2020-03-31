package com;

import com.house_guard.Common.*;

public class Model
{
    private final int MAX = 4;
    private String[] _digitArray;
    private int _currentDigit = 3;

    public Model()
    {
        _digitArray = new String[4];
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
        if(value == Types.BACK)
        {
            if(_currentDigit == 0)
            {
                if(_digitArray[_currentDigit] == Types.EMPTY)
                {
                    _currentDigit++;
                    _digitArray[_currentDigit] = Types.EMPTY;
                }
                else
                {
                    _digitArray[_currentDigit] = Types.EMPTY;
                    _currentDigit++;
                }
            }
            else if(_currentDigit == 3)
            {
                _digitArray[_currentDigit] = Types.EMPTY;
            }
            else
            {
                if(_digitArray[_currentDigit] == Types.EMPTY)
                {
                    _currentDigit++;
                }
                _digitArray[_currentDigit] = Types.EMPTY;
            }

        }
        else if(value == Types.CLEAR)
        {
            initModel(Types.EMPTY);
            _currentDigit = 3;
        }
        else
        {
            if(_currentDigit > 0)
            {
                _digitArray[_currentDigit] = value;
                _currentDigit--;
            } 
            else if(_currentDigit == 0) 
            {
                _digitArray[_currentDigit] = value;
            }
        }
        return _digitArray;
    }

    public Integer checkPass()
    {
        Integer digits = 0;
        digits += Integer.parseInt(_digitArray[0]) * 1000;
        digits += Integer.parseInt(_digitArray[1]) * 100;
        digits += Integer.parseInt(_digitArray[2]) * 10;
        digits += Integer.parseInt(_digitArray[3]);
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

    public String setModelStateOn()
    {
        return Types.ON;
    }

    public String setModelStateOFF()
    {
        return Types.OFF;
    }
}
