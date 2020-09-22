package com.house_guard.Common;

public class Account {
    private String role;
    private String email;

    public Account(String role, String email) {
        this.role = role;
        this.email = email;
    }

    public Account() {
        this.role = "N/A";
        this.email = "N/A";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail() {
        this.email = email;
    }
}