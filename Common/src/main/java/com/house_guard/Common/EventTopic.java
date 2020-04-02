package com.house_guard.Common;

public class EventTopic extends Topic
{
    public String getComponent()
    {
        return component;
    }
    public void setComponent(String component)
    {
        this.component = component;
    }
    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
    public String getTime()
    {
        return time;
    }
    public void setTime(String time)
    {
        this.time = time;
    }
    private String component;
    private String message;
    private String time;

}
