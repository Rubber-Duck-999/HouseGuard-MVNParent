package com.house_guard.Common;

public class StatusUP extends Topic {
    private String _accessGranted;
    private String _accessBlocked;
    private String _state;
    private String _user;

    public StatusUP() {

    }
    public String getGranted()
    {
        return _accessGranted;
    }
    public void setGranted(String granted)
    {
        this._accessGranted = granted;
    }

    public String getBlocked() {
        return _accessBlocked;
    }

    public void setBlocked(String blocked) {
        this._accessBlocked = blocked;
    }

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        this._state = state;
    }

    public String getUser() {
        return _user;
    }

    public void setUser(String user) {
        this._user = user;
    }
}
