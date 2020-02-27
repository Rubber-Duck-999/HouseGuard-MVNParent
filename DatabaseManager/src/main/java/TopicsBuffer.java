package com.house_guard.database_manager;

import java.util.*;
import java.util.logging.Logger;
import java.lang.*;
import com.house_guard.Common.Types;

public class TopicsBuffer {
    private List<TopicRabbitmq> _topics;
    private Logger LOGGER;
    private int _index_list;
    private boolean _listNotEmpty;
    private boolean exit = false;

    public void AddToList(TopicRabbitmq topic) {
        _topics.add(_index_list, topic);
        LOGGER.info("Added topic to list");
        _index_list++;
        _listNotEmpty = true;
    }

    public boolean ConvertTopics(TopicRabbitmq topic) {
        LOGGER.info("Converting topics = " + topic.GetRoutingKey());
        boolean type_found = true;
        if(topic.GetRoutingKey().equals(Types.EVENT_TOPIC_UP)) {
            LOGGER.info("Received a = " + topic.GetRoutingKey());
        } else if(topic.GetRoutingKey().equals(Types.EVENT_TOPIC_SYP)) {
            LOGGER.info("Received a = " + topic.GetRoutingKey());
        } else if(topic.GetRoutingKey().equals(Types.EVENT_TOPIC_DBM)) {
            LOGGER.info("Received a = " + topic.GetRoutingKey());
        } else if(topic.GetRoutingKey().equals(Types.EVENT_TOPIC_NAC)) {
            LOGGER.info("Received a = " + topic.GetRoutingKey());
        } else if(topic.GetRoutingKey().equals(Types.EVENT_TOPIC_EVM)) {
            LOGGER.info("Received a = " + topic.GetRoutingKey());
        } else if(topic.GetRoutingKey().equals(Types.EVENT_TOPIC_FH)) {
            LOGGER.info("Received a = " + topic.GetRoutingKey());
        } else if(topic.GetRoutingKey().equals(Types.REQUEST_DATABASE_TOPIC)) {
            LOGGER.info("Received a = " + topic.GetRoutingKey());
        } else {
            type_found = false;
        }
        return type_found;
    }

    public void SortList() {
        if(_listNotEmpty) {
            LOGGER.fine("List isn't empty, will sort");
            int i = _index_list - 1;
            while(i >= 0 && !exit) {
                try {
                    TopicRabbitmq local = _topics.get(i);
                    if(local.GetValidity()) {
                        LOGGER.info("Removing : " + i + ", Key: " + local.GetRoutingKey());
                        if (ConvertTopics(local) == false) {
                            local.SetInValidTopic();
                        }
                    }
                    i--;
                } catch(IndexOutOfBoundsException e) {
                    LOGGER.severe("We are out of bounds on topics list: " + e);
                    exit = true;
                }

            }
        }
    }

    public TopicsBuffer(Logger log) {
        LOGGER = log;
        _topics = new ArrayList<TopicRabbitmq>();
        _listNotEmpty = false;
        _index_list = 0;
    }
}
