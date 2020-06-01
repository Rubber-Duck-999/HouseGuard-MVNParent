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
    public String getEventTypeId()
    {
        return event_type_id;
    }
    public void setEventTypeId(String event_type_id)
    {
        this.event_type_id = event_type_id;
    }
    private String component;
    private String message;
    private String time;
    private String event_type_id;

}
