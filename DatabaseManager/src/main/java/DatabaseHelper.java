package com.house_guard.database_manager;

import java.sql.*;
import java.util.logging.Logger;
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
            this.run();
        } catch(SQLException e) {
            _LOGGER.severe("A connection could not be established because of :");
        }
        
    }

    private void run() throws SQLException {
        Statement statement = _connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + _username);
        _LOGGER.info("Printing schema for table: " + resultSet.getMetaData().getTableName(1));
    }

    private void message(String event_type, Types.Component com, String message, 
                            String time, int severity) throws SQLException {
        Statement statement = _connection.createStatement();
        ResultSet resultSet = statement.executeQuery("INSERT INTO event (event_type, component, 
                                                    message, time, severity) VALUES (" +
                                                    "'" + event_type + "', " + "'" + com + "', " + 
                                                    "'" + message + "', " + "'" + time + "', " +
                                                    "'" + severity + "')");
        //_LOGGER.info("Result of event table pull: " + resultSet.getMetaData().)
    }
}