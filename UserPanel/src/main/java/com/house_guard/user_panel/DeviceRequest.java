package com.house_guard.user_panel;	

public class DeviceRequest extends Topic	
{	
    private Integer id;	
    private String name;	
    private String mac;	

    public Integer getId()	
    {	
        return id;	
    }	
    public void setId(Integer _id)	
    {	
        this.id = _id;	
    }	
    public String getName()	
    {	
        return name;	
    }	
    public void setName(String _name)	
    {	
        this.name = _name;	
    }	

    public String getMac()	
    {	
        return mac;	
    }	

    public void setMac(String _mac)	
    {	
        this.mac = _mac;	
    }	
}