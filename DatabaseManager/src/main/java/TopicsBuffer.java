package com.house_guard.database_manager;

import java.util.*;
import java.sql.*;
import java.util.logging.Logger;
import java.lang.*;
import com.house_guard.Common.*;
import java.util.Vector;

public class TopicsBuffer {
    private List<TopicRabbitmq> _topics;
    private Logger LOGGER;
    private int _index_list;
    private boolean _listNotEmpty;
    private boolean exit = false;
    private DatabaseHelper _db;

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
                    i--;
                    _index_list--;
                } catch(IndexOutOfBoundsException e) {
                    LOGGER.severe("We are out of bounds on topics list: " + e);
                    exit = true;
                }

            }
        }
    }

    public DeviceResponse GetDeviceData(DeviceRequest request) {
        return _db.getDevice(request.getId(), request.getName(), request.getMac());
    }


    public Vector<DataInfoTopic> GetEventData(RequestDatabase request) {
        return _db.getEventMessages(request.getRequest_Id(), request.getMessage(),
            request.getTime_From(), request.getTime_To());
    }

    public TopicsBuffer(Logger log, String password) {
        LOGGER = log;
        _topics = new ArrayList<TopicRabbitmq>();
        _listNotEmpty = false;
        _index_list = 0;
        _db = new DatabaseHelper(log, password);
    }
}
