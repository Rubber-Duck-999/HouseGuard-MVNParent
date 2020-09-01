package com.house_guard.Common;

import java.util.Map;

public class FailureDatabase extends Topic
{
    private String time;
    private String type_of_failure;

    public FailureDatabase(String time, String type) 
    {
        this.time = time;
        this.type_of_failure = type;
    }

    public String getTime()
    {
        return time;
    }
    public void setTime(String time)
    {
        this.time = time;
    }
    public String getTypeOfFailure()
    {
        return type_of_failure;
    }
    public void setTypeOfFailure(String type)
    {
        this.type_of_failure = type;
    }
}
