package com.house_guard.user_panel;

public class Account {
    private String role;
    private String email;

    public Account(String role, String email) {
        this.role = role;
        this.email = email;
    }

    public Account() {
        this.role = "cheese";
        this.email = "cheese";
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