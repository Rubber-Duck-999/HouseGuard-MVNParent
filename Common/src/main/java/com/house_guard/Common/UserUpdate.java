package com.house_guard.Common;

public class UserUpdate extends Topic
{
    private String username;
    private String role;
    private String email;
    private String pincode;
    private String state;

    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
    public String getRole()
    {
        return role;
    }
    public void setRole(String role)
    {
        this.role = role;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getPincode()
    {
        return pincode;
    }
    public void setPincode(String pincode)
    {
        this.pincode = pincode;
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
