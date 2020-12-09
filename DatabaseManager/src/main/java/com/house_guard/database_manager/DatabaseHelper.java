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
    private boolean _setup;

    /*
    +---------------+-------------+------+-----+---------+----------------+
    | Field         | Type        | Null | Key | Default | Extra          |
    +---------------+-------------+------+-----+---------+----------------+
    | id            | int(11)     | NO   | PRI | NULL    | auto_increment |
    | routing_key   | varchar(50) | NO   |     | NULL    |                |
    | component     | varchar(5)  | NO   |     | NULL    |                |
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
    | role          | varchar(15) | NO    |     | NULL    |                |  
    | email         | varchar(50) | NO    |     | NULL    |                | 
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
            removeOldData();
            _setup = true;
        } catch(SQLException e) {
            _LOGGER.severe("A connection could not be established because of : " + e );
            e.printStackTrace();
            _setup = false;
        }
    }

    public boolean getSetup() {
        return _setup;
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

    public String getCommonEvent() {
        String event_type_id = "";
        try {
            PreparedStatement _prepared = _connection.prepareStatement("SELECT event_type_id FROM event GROUP " +
                "BY event_type_id ORDER BY COUNT(*) DESC LIMIT 1");
            ResultSet rs = _prepared.executeQuery();
            while(rs.next()) {
                event_type_id = rs.getString("event_type_id");
            }
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
        return event_type_id;
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
                                            "time_sent, time_received, event_type_id) VALUES " +
                                            "(?, ?, ?, ?, ?)");
            _prepared.setString(1, input.getRoutingKey());
            _prepared.setString(2, input.getComponent());
            _prepared.setTimestamp(3, Timestamp.valueOf(input.getTimeSent()));
            _prepared.setTimestamp(4, Timestamp.valueOf(input.getTimeReceived()));
            _prepared.setString(5, input.getEventTypeId());
            this.Execute(_prepared);
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
    }

    public void addUser(UserUpdate user) {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("INSERT INTO users (username, role, email, pin_code) VALUES " +
                                            "(?, ?, ?, ?)");
            _prepared.setString(1, user.getUsername());
            _prepared.setString(2, user.getRole());
            _prepared.setString(3, user.getEmail());
            _prepared.setString(3, user.getPincode());
            this.Execute(_prepared);
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
    }

    public void editUser(UserUpdate user) {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("UPDATE users SET pin_code=? WHERE " +
                                            "username=?");
            _prepared.setString(1, user.getPincode());
            _prepared.setString(2, user.getUsername());
            Execute(_prepared);
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
    }

    public void removeUser(UserUpdate user) {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("DELETE FROM users WHERE username=?");
            _prepared.setString(1, user.getUsername());
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

    public void updateState(boolean stateUpdate) {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("UPDATE state SET state=? WHERE " +
                                            "id=?");
            _prepared.setBoolean(1, stateUpdate);
            _prepared.setInt(2, 1);
            Execute(_prepared);
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        }
    }

    public boolean getState()
        throws SQLException {
            _LOGGER.info("Creating statement for getting state of alarm");
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM state WHERE id=?");
            _prepared.setInt(1, 1);
            ResultSet rs = _prepared.executeQuery();
            boolean state = true;
            while(rs.next()) {
                return rs.getBoolean("state");
            }
            return state;
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

    public int getMessageCount(String event_type_id, LocalDateTime dateFrom, LocalDateTime dateTo)
    throws SQLException {
        _LOGGER.info("Creating statement for finding message count of: " + event_type_id);
        PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE event_type_id=?" +
                                      " AND time_sent >=? AND time_sent <= ?");
        _prepared.setString(1, event_type_id);
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
                local.setRole(rs.getString("role"));
                local.setResult("PASS");
                count = 1;
            }
            if(count == 0) {
                _LOGGER.info("Didn't find any data");
                local.setUser("N/A");
                local.setRole("N/A");
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

    public EmailResponse getRole(String role) {
        EmailResponse email = new EmailResponse();
        try {
            _LOGGER.info("Creating statement for finding matching user roles");
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM users WHERE role=?");
            _prepared.setString(1, role);
            //
            ResultSet rs = _prepared.executeQuery();
            int i = 0;
            while(rs.next()) {
                Account account = new Account(rs.getString("role"), rs.getString("email"));
                email.addAccount(account);
                i = 1;
            }
            if(i == 0) {
                Account account = new Account("N/A", "N/A");
                email.addAccount(account);
            }
        } catch(SQLException e) {
            Account account = new Account("N/A", "N/A");
            email.addAccount(account);
            _LOGGER.severe("Error");
        } catch(Exception e) {
            Account account = new Account("N/A", "N/A");
            email.addAccount(account);
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
        }
        return email;
    }


    public Vector<DataInfoTopic> getEventMessages(Integer id, String event_type_id, String dateFrom, String dateTo) {
        Vector<DataInfoTopic> localVector = new Vector<>();
        try {
            _LOGGER.info("Creating statement for returning messages of: " + event_type_id + " from: " +
                         dateFrom + ", to: " + dateTo);
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE event_type_id=?" +
                                          " AND time_sent >=? AND time_sent <=?");
            _prepared.setString(1, event_type_id);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
