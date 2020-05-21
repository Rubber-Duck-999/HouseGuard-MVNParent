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
    private List<TopicRabbitmq> _topics;
    private Logger LOGGER;
    private int _index_list;
    private boolean _listNotEmpty;
    private boolean exit = false;
    private DatabaseHelper _db;
    private StatusDBM _status;
    private Integer _dailyEvents;
    private Integer _dailyRequests;
    private String _day;
    private SimpleDateFormat _simpleDateformat;

    public void AddToList(TopicRabbitmq topic) {
        _topics.add(_index_list, topic);
        LOGGER.info("Added topic to list: " + topic.getRoutingKey());
        _index_list++;
        _listNotEmpty = true;
    }

    public void SortList() {
        if(_listNotEmpty) {
            LOGGER.fine("List isn't empty, will sort");
            int i = _index_list - 1;
            while(i >= 0 && !exit) {
                try {
                    _db.addMessage(_topics.get(i));
                    Date now = new Date();
                    String day = _simpleDateformat.format(now);
                    if(day.equals(_day)) {
                        _dailyEvents++;
                    } else {
                        _dailyEvents = 0;
                    }
                    i--;
                    _index_list--;
                } catch(IndexOutOfBoundsException e) {
                    LOGGER.severe("We are out of bounds on topics list: " + e);
                    exit = true;
                }

            }
        }
    }

    public void CreateDevice(DeviceUpdate device)
    {
        String state = device.getState();
        LOGGER.info("The device will be " + state);
        if(state.equals(Types.ADD_STATE)) {
            LOGGER.info("Add device");
            _db.addDevice(device);
        } else if(state.equals(Types.EDIT_STATE)) {
            LOGGER.info("Edit device");
            _db.editDevice(device);
        } else if (state.equals(Types.REMOVE_STATE)) {
            LOGGER.info("Remove device");
            _db.removeDevice(device);
        } else {
            LOGGER.info("What happened here!");
        }
        _db.printDevicesTableData();
    }

    public DeviceResponse GetDeviceData(DeviceRequest request) {
        return _db.getDevice(request.getId(), request.getName(), request.getMac());
    }


    public Vector<DataInfoTopic> GetEventData(RequestDatabase request) {
        Date now = new Date();
        String day = _simpleDateformat.format(now);
        if(day.equals(_day)) {
            _dailyRequests++;
        } else {
            _dailyRequests = 0;
        }
        return _db.getEventMessages(request.getRequest_Id(), request.getMessage(),
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
        _topics = new ArrayList<TopicRabbitmq>();
        _listNotEmpty = false;
        _index_list = 0;
        _db = new DatabaseHelper(log, password);
        _status = new StatusDBM();
        _dailyEvents = 0;
        _dailyRequests = 0;
    }
}
