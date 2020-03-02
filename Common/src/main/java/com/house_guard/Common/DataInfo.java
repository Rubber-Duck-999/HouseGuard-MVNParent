package com.house_guard.Common;

public class DataInfo extends Topic
{
    private Integer id;
    private String message;
    private String time_sent;

    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getTimeSent()
    {
        return time_sent;
    }
    public void setTimeSent(String time_sent)
    {
        this.time_sent = time_sent;
    }

}
