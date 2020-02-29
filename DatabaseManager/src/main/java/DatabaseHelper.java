package com.house_guard.database_manager;

import java.sql.*;
import java.util.logging.Logger;
import java.time.LocalDateTime;
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
                                                                    "(?, ?, ?, TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'), " + 
                                                                    "?, TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'))");
            _prepared.setString(1, input.getRoutingKey());
            _prepared.setString(2, input.getComponent());
            _prepared.setString(3, input.getTopicMessage());
            //_prepared.setDate(4, input.getTimeSent());
            _prepared.setInt(5, input.getSeverity());
            //_prepared.setDate(6, input.getTimeReceived());
            _prepared.executeUpdate();
        } catch(SQLException e) {
            _LOGGER.severe("Error");
        }
    }

    public int getMessageCount(String component, LocalDateTime dateFrom, LocalDateTime dateTo) 
                throws SQLException {
        PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM event WHERE message=?" +
                                                                   " AND time_sent >=? AND time_sent <= ?");
        _prepared.setString(1, "");
        //_prepared.setString(2, dateFrom);
        //_prepared.setString(3, dateTo);
        ResultSet rs = _prepared.executeQuery();
        int count = 0;
        if(rs.next())
        {
            _LOGGER.info(String.valueOf(rs.getInt("id")));
            count++;
        }
        return count;
    }
}