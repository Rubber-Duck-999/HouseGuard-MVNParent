package com.house_guard.database_manager;

import java.util.logging.Logger;
import com.house_guard.Common.Types; 
import com.house_guard.Common.*;
import java.util.Vector;
import java.util.ArrayList;


public class TopicsBuffer {
    private Logger LOGGER;
    private DatabaseHelper _db;
    private StatusDBM _status;

    public void AddTopic(TopicRabbitmq topic) {
        _status.setDailyEvents(_status.getDailyEvents() + 1);
        _db.addMessage(topic);
    }

    public EmailResponse GetEmails(EmailRequest email_request)
    {
        EmailResponse email = new EmailResponse();
        try {
            String role = email_request.getRole();
            LOGGER.info("Looking for users with role of " + role);
            switch(role) {
                case Types.BOTH_ROLE:
                    LOGGER.info("Both check");
                    email = _db.getRole("*");
                    break;
                case Types.ADMIN_ROLE:
                    LOGGER.info("Admin check");
                    email = _db.getRole("Admin");
                    break;
                default:
                    LOGGER.info("What happened here!");
                    break;
            }
            ArrayList<Account> Accounts = email.getAccounts();
            for(int i = 0; i < Accounts.size(); i++) {
                LOGGER.info("Row: " + i + ", Role: " + 
                Accounts.get(i).getRole() + ", Email: " + Accounts.get(i).getEmail());
            }
            LOGGER.info("Returning email to publish");
        } catch(Exception e) {
            LOGGER.severe("Error: " + e);
            e.printStackTrace();
        }
        return email;
    }

    public void CreateUser(UserUpdate user)
    {
        String state = user.getState();
        LOGGER.info("The device will be " + state);
        switch(state) {
            case Types.ADD_STATE:
                _db.addUser(user);
                break;
            case Types.EDIT_STATE:
                _db.editUser(user);
                break;
            case Types.REMOVE_STATE:
                _db.removeUser(user);
                break;
            default:
                LOGGER.info("What happened here!");
                break;
        }
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
        _status.setCommonEvent(_db.getCommonEvent());
        return _status;
    }

    public TopicsBuffer(Logger log, String password) {
        LOGGER = log;
        _db = new DatabaseHelper(log, password);
        _status = new StatusDBM();
    }

    public boolean getSetup() {
        return _db.getSetup();
    }
}
