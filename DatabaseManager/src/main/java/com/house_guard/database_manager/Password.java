package com.house_guard.database_manager;

public class Password {
    private String rabbitmq;
    private String sql;

    public String getRabbitmq() {
        return rabbitmq;
    }

    public void setRabbitmq(String password) {
        this.rabbitmq = password;
    }

    public String getSQL() {
        return sql;
    }

    public void setSQL(String password) {
        this.sql = password;
    }
}