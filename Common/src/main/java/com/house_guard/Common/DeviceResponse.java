package com.house_guard.Common;

public class DeviceResponse extends Topic
{
    private Integer id;
    private String name;
    private String mac;
    private String status;

    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
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
}