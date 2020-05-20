package com.house_guard.Common;

public class StatusDBM extends Topic {
    private Integer _dailyEvents;
    private Integer _totalEvents;
    private String  _commonEvent;
    private Integer _dailyDataRequests;

    public StatusDBM() {

    }
    public Integer getDailyEvents()
    {
        return _dailyEvents;
    }
    public void setDailyEvents(Integer dailyEvents)
    {
        this._dailyEvents = dailyEvents;
    }
    public Integer getTotalEvents()
    {
        return _totalEvents;
    }
    public void setTotalEvents(Integer totalEvents)
    {
        this._totalEvents = totalEvents;
    }
    public String getCommonEvent()
    {
        return _commonEvent;
    }
    public void setCommonEvent(String commonEvent)
    {
        this._commonEvent = commonEvent;
    }
    public Integer getDailyDataRequests()
    {
        return _dailyDataRequests;
    }
    public void setDailyDataRequests(Integer dailyDataRequests)
    {
        this._dailyDataRequests = dailyDataRequests;
    }

}
