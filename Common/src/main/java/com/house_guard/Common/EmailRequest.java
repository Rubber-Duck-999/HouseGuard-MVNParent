package com.house_guard.Common;

public class EmailRequest extends Topic {
    private String role;

    public String getRole() {
        return role;
    }

    public void setRoutingKey(String role) {
        this.role = role;
    }
}
