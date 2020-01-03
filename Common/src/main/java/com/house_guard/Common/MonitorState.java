package com.house_guard.Common;

public class MonitorState extends Topic
{
    private boolean state;

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

}
