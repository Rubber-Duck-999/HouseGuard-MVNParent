package com.house_guard.user_panel;

import java.sql.*;
import java.util.logging.Logger;
import com.house_guard.Common.*;

public class DatabaseHelper {
    private Connection _connection;
    private Logger _LOGGER;
    private boolean _setup;

    /*
    +---------------+-------------+-------+-----+---------+----------------+
    | Field         | Type        | Null  | Key | Default | Extra          |
    +---------------+-------------+-------+-----+---------+----------------+
    | id            | int(11)     | NO    | PRI | NULL    | auto_increment |
    | username      | varchar(50) | YES   |     | NULL    |                |
    | role          | varchar(15) | NO    |     | NULL    |                |  
    | email         | varchar(50) | NO    |     | NULL    |                | 
    | pin           | int(4)      | YES   |     | NULL    |                |
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
        String database_prefix = "jdbc:mysql://localhost/houseguard?";
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
            _setup = true;
        } catch(SQLException e) {
            _LOGGER.severe("A connection could not be established because of : " + e );
            e.printStackTrace();
            System.exit(1);
            _setup = false;
        }
    }

    public boolean getSetup() {
        return _setup;
    }


    private void Execute(PreparedStatement prepared) {
        try {
            _LOGGER.info("Creating statement for inserting data into table");
            prepared.executeUpdate();
        } catch(SQLException e) {
            _LOGGER.severe("Error: " + e);
        } catch(Exception e) {
            _LOGGER.severe("Error: " + e);
            e.printStackTrace();
            System.exit(0);
        }       
    }

    public void addUser(UserUpdate user) {
        try {
            PreparedStatement _prepared = _connection.prepareStatement("INSERT INTO users (username, role, email, pin) VALUES " +
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
            PreparedStatement _prepared = _connection.prepareStatement("UPDATE users SET pin=? WHERE " +
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

    public AccessResponse checkUser(Integer pin) {
        AccessResponse local = new AccessResponse();
        try {
            _LOGGER.info("Creating statement for finding matching user");
            PreparedStatement _prepared = _connection.prepareStatement("SELECT * FROM users WHERE pin=?");
            _prepared.setInt(1, pin);
            //
            ResultSet rs = _prepared.executeQuery();
            int count = 0;
            local.setId(1);
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
}
