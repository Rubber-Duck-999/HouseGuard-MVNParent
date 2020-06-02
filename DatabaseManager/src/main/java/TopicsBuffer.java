package com.house_guard.database_manager;

import java.util.*;
import java.util.logging.Logger;
import java.lang.*;
import com.house_guard.Common.Types; 
import com.house_guard.Common.*;
import java.util.Vector;

public class TopicsBuffer {
    private Logger LOGGER;
    private DatabaseHelper _db;
    private StatusDBM _status;

    public void AddTopic(TopicRabbitmq topic) {
        _status.setDailyEvents(_status.getDailyEvents() + 1);
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
        _status.setDailyDataRequests(_status.getDailyDataRequests() + 1);
        return _db.getEventMessages(request.getRequest_Id(), request.getEventTypeId(),
            request.getTime_From(), request.getTime_To());
    }

    public AccessResponse GetUser(RequestAccess access) {
        return _db.checkUser(access);
    }

    public StatusDBM GetStatus() {
        _status.setTotalEvents(_db.getTotalEvents());
        _status.setCommonEvent(_db.getCommonMessage());
        return _status;
    }

    public TopicsBuffer(Logger log, String password) {
        LOGGER = log;
        _db = new DatabaseHelper(log, password);
        _status = new StatusDBM();
    }
}
