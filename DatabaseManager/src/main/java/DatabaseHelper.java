package com.house_guard.database_manager;

import java.sql.*;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.Date;
import com.house_guard.Common.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class DatabaseHelper {
    private Connection _connection;
    private Logger _LOGGER;

    /*
    +---------------+-------------+------+-----+---------+----------------+
    | Field         | Type        | Null | Key | Default | Extra          |
    +---------------+-------------+------+-----+---------+----------------+
    | id            | int(11)     | NO   | PRI | NULL    | auto_increment |
    | routing_key   | varchar(50) | NO   |     | NULL    |                |
    | component     | varchar(5)  | NO   |     | NULL    |                |
    | message       | varchar(30) | NO   |     | NULL    |                |
    | time_sent     | datetime    | YES  |     | NULL    |                |
    | time_received | datetime    | YES  |     | NULL    |                |
    | event_type_id | varchar(7)  | YES  |     | NULL    |                |
    +---------------+-------------+------+-----+---------+----------------+
    */

    /*
    +---------------+-------------+-------+-----+---------+----------------+
    | Field         | Type        | Null  | Key | Default | Extra          |
    +---------------+-------------+-------+-----+---------+----------------+
    | id            | int(11)     | NO    | PRI | NULL    | auto_increment |
    | username      | varchar(50) | YES   |     | NULL    |                |
    | pin_code      | int(4)      | YES   |     | NULL    |                |
    +---------------+-------------+-------+-----+---------+----------------+
    */

    /*
    +---------------+--------------+------+-----+---------+----------------+
    | Field         | Type         | Null | Key | Default | Extra          |
    +---------------+--------------+------+-----+---------+----------------+
    | Name          | varchar(100) | YES  |     | NULL    |                |
    | Mac           | varchar(20)  | YES  |     | NULL    |                |
    | device_id     | int(11)      | NO   | PRI | NULL    | auto_increment |
    | status        | varchar(10)  | YES  |     | NULL    |                |
    +---------------+--------------+------+-----+---------+----------------+
    */

    public DatabaseHelper(Logger LOGGER, String password) {
        _LOGGER = LOGGER;
        String database_prefix = "jdbc:mysql://localhost/logs?";
        String database_suffix = "&zeroDateTimeBehavior=convertToNull";
        String user_entry = "user=";
        String password_entry = "&password=";
        String _username;
        String _password;
        try {
            _username = "access";
            _password = password;
            _connection = DriverManager.getConnection(database_prefix + user_entry +
                          _username + password_entry +
                          _password + database_suffix);
        } catch(SQLException e) {
            _LOGGER.severe("A connection could not be established because of : " + e );
            e.printStackTrace();
        }
    }

    private void removeOldData() {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("DELETE FROM event WHERE " +
                                            "time_sent<=?");
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime x = current.minusDays(3);
            Timestamp timestamp = Timestamp.valueOf(x);
            _prepared.setTimestamp(1, timestamp);
            Execute(_prepared);
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
    }

    public boolean checkUserPin(int pin) throws SQLException {
        _LOGGER.info("Creating statement for checking pin");
        PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM users WHERE pin_code=?");
        _prepared.setInt(1, pin);
        ResultSet rs = _prepared.executeQuery();
        boolean count = false;
        while(rs.next()) {
            count = true;
        }
        return count;
    }

    public Integer getTotalEvents() {
        Integer count = 0;
        try {
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event");
            ResultSet rs = _prepared.executeQuery();

            while(rs.next()) {
                count++;
            }
            String str = String.valueOf(count);
            _LOGGER.warning("Total " + str + " event records in DB");
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
            System.exit(0);
        }
        return count;
    }

    public String getCommonMessage() {
        String message = "";
        try {
            PreparedStatement _prepared = _connection.prepareStatement("SELECT message FROM event GROUP " +
                "BY message ORDER BY COUNT(*) DESC LIMIT 1");
            ResultSet rs = _prepared.executeQuery();
            while(rs.next()) {
                message = rs.getString("message");
            }
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
        return message;
    }

    public int getTotalComponentCount(String component)
    throws SQLException {
        _LOGGER.info("Creating statement for finding message count of: " + component);
        PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE component=?");
        _prepared.setString(1, component);
        ResultSet rs = _prepared.executeQuery();
        int count = 0;
        while(rs.next()) {
            count++;
        }
        return count;
    }

    private void Execute(PreparedStatement prepared) {
        try {
            _LOGGER.info("Creating statement for inserting data into event table");
            prepared.executeUpdate();
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
            System.exit(0);
        }       
    }

    public void addMessage(TopicRabbitmq input) {
        try{
            PreparedStatement _prepared = _connection.prepareStatement("INSERT INTO event (routing_key, component, " +
                                            "message,  time_sent, time_received, event_type_id) VALUES " +
                                            "(?, ?, ?, ?, ?, ?)");
            _prepared.setString(1, input.getRoutingKey());
            _prepared.setString(2, input.getComponent());
            _prepared.setString(3, input.getTopicMessage());
            _prepared.setTimestamp(4, Timestamp.valueOf(input.getTimeSent()));
            _prepared.setTimestamp(5, Timestamp.valueOf(input.getTimeReceived()));
            _prepared.setString(6, input.getEventTypeId());
            this.Execute(_prepared);
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
    }

    public void addDevice(DeviceUpdate device) {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("INSERT INTO devices (Name, Mac, status) VALUES " +
                                            "(?, ?, ?)");
            _prepared.setString(1, device.getName());
            _prepared.setString(2, device.getMac());
            _prepared.setString(3, device.getStatus());
            this.Execute(_prepared);
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
    }

    public void editDevice(DeviceUpdate device) {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("UPDATE devices SET Status=? WHERE " +
                                            "Mac=?");
            _prepared.setString(1, device.getStatus());
            _prepared.setString(2, device.getMac());
            Execute(_prepared);
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
    }

    public void removeDevice(DeviceUpdate device) {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("DELETE FROM devices WHERE Mac=?");
            _prepared.setString(1, device.getMac());
            this.Execute(_prepared);
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
    }

    public int getMessageCount(String message, LocalDateTime dateFrom, LocalDateTime dateTo)
    throws SQLException {
        _LOGGER.info("Creating statement for finding message count of: " + message);
        PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE message=?" +
                                      " AND time_sent >=? AND time_sent <= ?");
        _prepared.setString(1, message);
        _prepared.setTimestamp(2, Timestamp.valueOf(dateFrom));
        _prepared.setTimestamp(3, Timestamp.valueOf(dateTo));
        ResultSet rs = _prepared.executeQuery();
        int count = 0;
        while(rs.next()) {
            _LOGGER.info(String.valueOf(rs.getInt("id")));
            count++;
        }
        return count;
    }

    public int getComponentCount(String component, LocalDateTime dateFrom, LocalDateTime dateTo)
    throws SQLException {
        _LOGGER.info("Creating statement for finding component count of: " + component);
        PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE component=?" +
                                      " AND time_sent >=? AND time_sent <= ?");
        _prepared.setString(1, component);
        _prepared.setTimestamp(2, Timestamp.valueOf(dateFrom));
        _prepared.setTimestamp(3, Timestamp.valueOf(dateTo));
        ResultSet rs = _prepared.executeQuery();
        int count = 0;
        while(rs.next()) {
            _LOGGER.info(String.valueOf(rs.getInt("id")));
            count++;
        }
        return count;
    }

    public DeviceResponse getDevice(Integer id, String name, String mac) {
        DeviceResponse local = new DeviceResponse();
        try {
            _LOGGER.info("Creating statement for finding matching device for: " + name);
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM devices WHERE Name=?" +
                                          " OR Mac=?");
            _prepared.setString(1, name);
            _prepared.setString(2, mac);
            //
            ResultSet rs = _prepared.executeQuery();
            int count = 0;
            while(rs.next()) {
                local.setId(id);
                local.setName(rs.getString("Name"));
                local.setMac(rs.getString("Mac"));
                local.setStatus(rs.getString("status"));
                count = 1;
            }
            if(count == 0) {
                _LOGGER.info("Didn't find any data");
                local.setId(id);
                local.setName(name);
                local.setMac(mac);
                local.setStatus("UNKNOWN");
            }
        } catch(SQLException e) {
            _LOGGER.severe("Error");
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
        }
        return local;
    }

    public AccessResponse checkUser(RequestAccess access) {
        AccessResponse local = new AccessResponse();
        try {
            _LOGGER.info("Creating statement for finding matching user");
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM users WHERE pin_code=?");
            _prepared.setInt(1, access.getPin());
            //
            ResultSet rs = _prepared.executeQuery();
            int count = 0;
            local.setId(access.getId());
            while(rs.next()) {
                local.setUser(rs.getString("username"));
                local.setResult("PASS");
                count = 1;
            }
            if(count == 0) {
                _LOGGER.info("Didn't find any data");
                local.setUser("N/A");
                local.setResult("FAIL");
            }
        } catch(SQLException e) {
            _LOGGER.severe("Error");
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
        }
        return local;
    }


    public Vector<DataInfoTopic> getEventMessages(Integer id, String event_type_id, String dateFrom, String dateTo) {
        Vector<DataInfoTopic> localVector = new Vector<>();
        try {
            _LOGGER.info("Creating statement for returning messages of: " + event_type_id + " from: " +
                         dateFrom + ", to: " + dateTo);
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE event_type_id=?" +
                                          " AND time_sent >=? AND time_sent <=?");
            _prepared.setString(1, event_type_id);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date parsedDate = sdf.parse(dateFrom);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            _prepared.setTimestamp(2, timestamp);
            //
            parsedDate = sdf.parse(dateTo);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
            //
            _prepared.setTimestamp(3, timestamp);
            ResultSet rs = _prepared.executeQuery();
            int total = 0;
            while(rs.next()) {
                total++;
            }
            // Redo
            rs = _prepared.executeQuery();
            int count = 0;
            while(rs.next()) {
                DataInfoTopic data = new DataInfoTopic();
                data.setId(id);
                data.setTotalMessage(total);
                data.setMessageNum(count + 1);
                data.setTopicMessage(rs.getString("message"));
                data.setTimeSent(rs.getTimestamp("time_sent").toString());
                localVector.add(data);
                _LOGGER.info("Found record ID: " + data.getId());
                count++;
            }
            if(count == 0) {
                _LOGGER.info("Didn't find any data");
                DataInfoTopic data = new DataInfoTopic();
                data.setTotalMessage(1);
                data.setMessageNum(1);
                data.setId(id);
                data.setTopicMessage("No data found");
                data.setTimeSent(LocalDateTime.now().toString());
                localVector.add(data);
            }
        } catch(SQLException e) {
            _LOGGER.severe("Error");
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
        }
        return localVector;
    }
}
