package com.house_guard.Common;

public class RequestAccess extends Topic
{
    private Integer id;
    private Integer pin;

    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public Integer getPin()
    {
        return pin;
    }
    public void setPin(Integer pin)
    {
        this.pin = pin;
    }

    public RequestAccess(Integer id, Integer pin)
    {
        this.id = id;
        this.pin = pin;
    }

}
