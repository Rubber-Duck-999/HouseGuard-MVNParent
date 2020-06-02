package com.house_guard.database_manager;

import java.util.*;
import java.sql.*;
import java.util.logging.Logger;
import java.lang.*;
import com.house_guard.Common.Types;
import java.text.SimpleDateFormat; 
import com.house_guard.Common.*;
import java.util.Date;  
import java.util.Vector;

public class TopicsBuffer {
    private Logger LOGGER;
    private DatabaseHelper _db;
    private StatusDBM _status;
    private Integer _dailyEvents;
    private Integer _dailyRequests;
    private String _day;
    private SimpleDateFormat _simpleDateformat;

    private void CheckDay() {
        Date now = new Date();
        String day = _simpleDateformat.format(now);
        if(day.equals(_day)) {
            _dailyEvents++;
        } else {
            _dailyEvents = 0;
        }
    }

    public void AddTopic(TopicRabbitmq topic) {
        CheckDay();
        _db.addMessage(topic);
    }

    public void CreateDevice(DeviceUpdate device)
    {
        String state = device.getState();
        LOGGER.info("The device will be " + state);
        switch(state) {
            case Types.ADD_STATE:
                _db.addDevice(device);
                break;
            case Types.EDIT_STATE:
                _db.editDevice(device);
                break;
            case Types.REMOVE_STATE:
                _db.removeDevice(device);
                break;
            default:
                LOGGER.info("What happened here!");
                break;
        }
    }

    public DeviceResponse GetDeviceData(DeviceRequest request) {
        return _db.getDevice(request.getId(), request.getName(), request.getMac());
    }


    public Vector<DataInfoTopic> GetEventData(RequestDatabase request) {
        CheckDay();
        return _db.getEventMessages(request.getRequest_Id(), request.getEventTypeId(),
            request.getTime_From(), request.getTime_To());
    }

    public AccessResponse GetUser(RequestAccess access) {
        return _db.checkUser(access);
    }

    public StatusDBM getStatus() {
        _status.setDailyEvents(_dailyEvents);
        _status.setTotalEvents(_db.getTotalEvents());
        _status.setCommonEvent(_db.getCommonMessage());
        _status.setDailyDataRequests(_dailyRequests);
        return _status;
    }

    public TopicsBuffer(Logger log, String password) {
        Date now = new Date();
        _simpleDateformat = new SimpleDateFormat("EEEE");
        _day = _simpleDateformat.format(now);
        LOGGER = log;
        _dailyEvents = _dailyRequests = 0;
        _db = new DatabaseHelper(log, password);
        _status = new StatusDBM();
    }
}
