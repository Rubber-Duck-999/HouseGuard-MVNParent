package com.house_guard.Common;

public class DeviceUpdate extends Topic
{
    private String name;
    private String mac;
    private String status;
    private String state;

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getMac()
    {
        return mac;
    }
    public void setMac(String mac)
    {
        this.mac = mac;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public String getState()
    {
        return state;
    }
    public void setState(String state)
    {
        this.state = state;
    }
}
