package com.house_guard.Common;

public class RequestDatabase extends Topic
{
    private Integer request_id;
    private String message;
    private String time_from;
    private String time_to;

    public Integer getRequest_Id()
    {
        return request_id;
    }
    public void setRequest_Id(Integer id)
    {
        this.request_id = id;
    }

    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getTime_From()
    {
        return time_from;
    }
    public void setTime_From(String time_from)
    {
        this.time_from = time_from;
    }

    public String getTime_To()
    {
        return time_to;
    }
    public void setTime_To(String time_to)
    {
        this.time_to = time_to;
    }
}
