package com.house_guard.internal_system_controller;

public class RequestData {

    private String event_type_id;
    private String time_from;
    private String time_to;

    public RequestData(String event_type_id,
                        String time_from, String time_to)
    {
        this.time_from = time_from;
        this.time_to = time_to;
        this.event_type_id = event_type_id;
    }

    public String getEventTypeId()
    {
        return event_type_id;
    }
    public void setEventTypeId(String event_type_id)
    {
        this.event_type_id = event_type_id;
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
