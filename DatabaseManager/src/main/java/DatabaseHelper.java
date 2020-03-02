package com.house_guard.database_manager;

import java.sql.*;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.util.Vector;
import com.house_guard.Common.*;

public class DatabaseHelper {
    private Connection _connection;
    private final String database_prefix = "jdbc:mysql://localhost/example?";
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
    | event_type    | varchar(20) | NO   |     | NULL    |                |
    | component     | varchar(5)  | NO   |     | NULL    |                |
    | message       | varchar(30) | NO   |     | NULL    |                |
    | severity      | int(11)     | NO   |     | NULL    |                |
    | id            | int(11)     | NO   | PRI | NULL    | auto_increment |
    | time_sent     | datetime    | YES  |     | NULL    |                |
    | time_received | datetime    | YES  |     | NULL    |                |
    +---------------+-------------+------+-----+---------+----------------+
    */

    public DatabaseHelper(Logger LOGGER) {
        _LOGGER = LOGGER;
        try {
            _connection = DriverManager.getConnection(database_prefix + user_entry + 
                                                    _username + password_entry + 
                                                    _password + database_suffix);
        } catch(SQLException e) {
            _LOGGER.severe("A connection could not be established because of :");
        }
        
    }

    private void printTableData() throws SQLException {
        Statement statement = _connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM event");
        _LOGGER.info("Printing schema for table: " + resultSet.getMetaData().getTableName(1));
    }

    public void addMessage(TopicRabbitmq input) {
        try {
            _LOGGER.info("Creating statement for inserting data into event table");
            Statement statement = _connection.createStatement();
            PreparedStatement _prepared = _connection.prepareStatement("INSERT INTO event (event_type, component, " + 
                                                                    "message, severity, time_sent, time_received) VALUES " +
                                                                    "(?, ?, ?, ?, ?, ?)");
            _prepared.setString(1, input.getRoutingKey());
            _prepared.setString(2, input.getComponent());
            _prepared.setString(3, input.getTopicMessage());
            _prepared.setTimestamp(4, Timestamp.valueOf(input.getTimeSent()));
            _prepared.setInt(5, input.getSeverity());
            _prepared.setTimestamp(6, Timestamp.valueOf(input.getTimeReceived()));
            _prepared.executeUpdate();
        } catch(SQLException e) {
            _LOGGER.severe("Error");
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
        if(rs.next())
        {
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
        if(rs.next())
        {
            _LOGGER.info(String.valueOf(rs.getInt("id")));
            count++;
        }
        return count;
    }

    public int getSeverityCount(int severity, LocalDateTime dateFrom, LocalDateTime dateTo) 
                throws SQLException {
        _LOGGER.info("Creating statement for finding severity count of: " + severity);
        PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE severity=?" +
                                                                   " AND time_sent >=? AND time_sent <= ?");
        _prepared.setInt(1, severity);
        _prepared.setTimestamp(2, Timestamp.valueOf(dateFrom));
        _prepared.setTimestamp(3, Timestamp.valueOf(dateTo));
        ResultSet rs = _prepared.executeQuery();
        int count = 0;
        if(rs.next())
        {
            _LOGGER.info(String.valueOf(rs.getInt("id")));
            count++;
        }
        return count;
    }

    
    public Vector<DataInfo> getMessages(String message, LocalDateTime dateFrom, LocalDateTime dateTo) 
                throws SQLException {
        _LOGGER.info("Creating statement for finding severity count of: " + message);
        PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE message=?" +
                                                                   " AND time_sent >=? AND time_sent <= ?");
        _prepared.setString(1, message);
        _prepared.setTimestamp(2, Timestamp.valueOf(dateFrom));
        _prepared.setTimestamp(3, Timestamp.valueOf(dateTo));
        ResultSet rs = _prepared.executeQuery();
        int count = 0;
        Vector<DataInfo> vector = new Vector<>();
        if(rs.next())
        {
            DataInfo data = new DataInfo();
            data.setId(rs.getInt("id"));
            data.setMessage(rs.getString("message"));
            data.setTimeSent(rs.getString("time_sent"));
            vector.add(data);
            _LOGGER.info("Found record: " + data.getId());
            count++;
        }
        return vector;
    }
}