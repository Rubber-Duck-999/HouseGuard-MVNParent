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
    private final String database_prefix = "jdbc:mysql://192.168.0.25/logs?";
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

    public DatabaseHelper(Logger LOGGER, String password) {
        _LOGGER = LOGGER;
        try {
            _username = "root";
            _password = password;
            _connection = DriverManager.getConnection(database_prefix + user_entry +
                          _username + password_entry +
                          _password + database_suffix);
        } catch(SQLException e) {
            _LOGGER.severe("A connection could not be established because of : " + e );
            e.printStackTrace();
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
            if(_connection == null) {
                _LOGGER.severe("Connection null");
            }
            PreparedStatement _prepared = _connection.prepareStatement("INSERT INTO event (event_type, component, " +
                                          "message, severity, time_sent, time_received) VALUES " +
                                          "(?, ?, ?, ?, ?, ?)");
            _prepared.setString(1, input.getRoutingKey());
            _prepared.setString(2, input.getComponent());
            _prepared.setString(3, input.getTopicMessage());
            _prepared.setTimestamp(5, Timestamp.valueOf(input.getTimeSent()));
            _prepared.setInt(4, 0);
            _prepared.setTimestamp(6, Timestamp.valueOf(input.getTimeReceived()));
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


    public Vector<DataInfoTopic> getMessages(Integer id, String message, String dateFrom, String dateTo) {
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
            while(rs.next())
            {
                DataInfoTopic data = new DataInfoTopic();
                data.setId(id);
                data.setTopicMessage(rs.getString("message"));
                data.setTimeSent(rs.getTimestamp("time_sent").toLocalDateTime());
                localVector.add(data);
                _LOGGER.info("Found record ID: " + data.getId());
                count++;
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