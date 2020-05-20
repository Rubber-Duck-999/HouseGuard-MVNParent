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
    private final String database_prefix = "jdbc:mysql://localhost/logs?";
    private final String database_suffix = "&zeroDateTimeBehavior=convertToNull";
    private final String user_entry = "user=";
    private final String password_entry = "&password=";
    private String _username;
    private String _password;
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
        try {
            _username = "access";
            _password = password;
            _connection = DriverManager.getConnection(database_prefix + user_entry +
                          _username + password_entry +
                          _password + database_suffix);
            printTableData();
            String str = String.valueOf(getTotalComponentCount("FH"));
            _LOGGER.warning("FH has " + str + " records");
            //
            str = String.valueOf(getTotalComponentCount("CM"));
            _LOGGER.warning("CM has " + str + " records");
            //
            str = String.valueOf(getTotalComponentCount("SYP"));
            _LOGGER.warning("SYP has " + str + " records");
            //
            str = String.valueOf(getTotalComponentCount("EVM"));
            _LOGGER.warning("EVM has " + str + " records");
            //
            str = String.valueOf(getTotalComponentCount("NAC"));
            _LOGGER.warning("NAC has " + str + " records");
            //
            str = String.valueOf(getTotalComponentCount("UP"));
            _LOGGER.warning("UP has " + str + " records");
        } catch(SQLException e) {
            _LOGGER.severe("A connection could not be established because of : " + e );
            e.printStackTrace();
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

    public void deleteComponent(String component)
    throws SQLException {
        _LOGGER.info("Creating statement for deleting message of component: " + component);
        PreparedStatement _prepared = _connection.prepareStatement("DELETE FROM event WHERE component=?");
        _prepared.setString(1, component);
        ResultSet rs = _prepared.executeQuery();
    }

    private void printTableData() throws SQLException {
        PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event");
        ResultSet rs = _prepared.executeQuery();
        int count = 0;
        while(rs.next()) {
            count++;
        }
        String str = String.valueOf(count);
        _LOGGER.warning("Total " + str + " event records in DB");
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

    public void printDevicesTableData() {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM devices");
            ResultSet rs = _prepared.executeQuery();
            int count = 0;
            _LOGGER.info("### Devices ###");
            while(rs.next()) {
                _LOGGER.info("ID: " + count + " : " + rs.getString("Name") +
                    " : " + rs.getString("Mac") + 
                    " : " + rs.getString("status"));
                count++;
            }
            String str = String.valueOf(count);
            _LOGGER.warning("Total " + str + " device records in DB");
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
            System.exit(0);
        }
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

    public void addMessage(TopicRabbitmq input) {
        try {
            _LOGGER.info("Creating statement for inserting data into event table");
            if(_connection == null) {
                _LOGGER.severe("Connection null");
            }
            PreparedStatement _prepared = _connection.prepareStatement("INSERT INTO event (routing_key, component, " +
                                          "message,  time_sent, time_received) VALUES " +
                                          "(?, ?, ?, ?, ?)");
            _prepared.setString(1, input.getRoutingKey());
            _prepared.setString(2, input.getComponent());
            _prepared.setString(3, input.getTopicMessage());
            _prepared.setTimestamp(4, Timestamp.valueOf(input.getTimeSent()));
            _prepared.setTimestamp(5, Timestamp.valueOf(input.getTimeReceived()));
            _prepared.executeUpdate();
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void addDevice(DeviceUpdate device) {
        try {
            _LOGGER.info("Creating statement for adding device into devices table");
            if(_connection == null) {
                _LOGGER.severe("Connection null");
            }
            PreparedStatement _prepared = _connection.prepareStatement("INSERT INTO devices (Name, Mac, " +
                                          "status) VALUES " +
                                          "(?, ?, ?)");
            _prepared.setString(1, device.getName());
            _prepared.setString(2, device.getMac());
            _prepared.setString(3, device.getStatus());
            _prepared.executeUpdate();
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void editDevice(DeviceUpdate device) {
        try {
            _LOGGER.info("Creating statement for editing device in devices table changing status to " +
                device.getStatus() + " and " + device.getName());
            if(_connection == null) {
                _LOGGER.severe("Connection null");
            }
            PreparedStatement _prepared = _connection.prepareStatement("UPDATE devices SET Status=? WHERE " +
                                          "Mac=?");
            _prepared.setString(1, device.getStatus());
            _prepared.setString(2, device.getMac());
            _prepared.executeUpdate();
            //
            _prepared = _connection.prepareStatement("UPDATE devices SET Name=? WHERE " +
                                          "Mac=?");
            _prepared.setString(1, device.getName());
            _prepared.setString(2, device.getMac());
            _prepared.executeUpdate();
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void removeDevice(DeviceUpdate device) {
        try {
            _LOGGER.info("Creating statement for removing device in devices table");
            if(_connection == null) {
                _LOGGER.severe("Connection null");
            }
            PreparedStatement _prepared = _connection.prepareStatement("DELETE * FROM devices WHERE Mac=?");
            _prepared.setString(1, device.getMac());
            _prepared.executeUpdate();
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
            System.exit(0);
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


    public Vector<DataInfoTopic> getEventMessages(Integer id, String message, String dateFrom, String dateTo) {
        Vector<DataInfoTopic> localVector = new Vector<>();
        try {
            _LOGGER.info("Creating statement for returning messages of: " + message + " from: " +
                         dateFrom + ", to: " + dateTo);
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE message=?" +
                                          " AND time_sent >=? AND time_sent <= ?");
            _prepared.setString(1, message);
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
            int count = 0;
            while(rs.next()) {
                DataInfoTopic data = new DataInfoTopic();
                data.setId(id);
                data.setTopicMessage(rs.getString("message"));
                data.setTimeSent(rs.getTimestamp("time_sent").toLocalDateTime());
                localVector.add(data);
                _LOGGER.info("Found record ID: " + data.getId());
                count++;
            }
            if(count == 0) {
                _LOGGER.info("Didn't find any data");
                DataInfoTopic data = new DataInfoTopic();
                data.setId(id);
                data.setTopicMessage("No data found");
                data.setTimeSent(LocalDateTime.now());
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
