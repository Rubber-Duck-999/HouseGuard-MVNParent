package com.house_guard.Common;

public class AlarmResponse extends Topic
{
    private boolean state;
    public boolean getState()
    {
        return state;
    }
    public void setState(boolean state)
    {
        this.state = state;
    }
}
