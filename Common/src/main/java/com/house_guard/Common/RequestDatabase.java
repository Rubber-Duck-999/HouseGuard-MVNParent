package com.house_guard.Common;

public class RequestDatabase extends Topic
{
    private Integer id;
    private String message;
    private String time_from;
    private String time_to;

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

    public String getTimeFrom()
    {
        return time_from;
    }
    public void setTimeFrom(String time_from)
    {
        this.time_from = time_from;
    }

    public String getTimeTo()
    {
        return time_to;
    }
    public void setTimeTo(String time_to)
    {
        this.time_to = time_to;
    }
}
