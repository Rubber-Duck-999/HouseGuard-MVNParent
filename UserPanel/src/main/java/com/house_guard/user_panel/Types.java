package com.house_guard.user_panel;

public interface Types
{

    final String ONE = "1";
    final String TWO = "2";
    final String THREE = "3";
    final String FOUR = "4";
    final String FIVE = "5";
    final String SIX = "6";
    final String SEVEN = "7";
    final String EIGHT = "8";
    final String NINE = "9";
    final String ZERO = "0";

    final String BACK = "<";
    final String CLEAR = "X";

    final String EMPTY = "X";
    final String ENTER = "Enter";

    final int RESET = 0;
    final String OFF = "OFF";
    final String ON = "ON";
    final Integer MAX_ATTEMPTS = 3;

    final int ATTEMPTS_MAXED = 0;
    final int INCORRECT = 1;
    final int CORRECT = 2;
    final int TIMEOUT = 3;

    //
    final String MONITOR_STATE_TOPIC = "Monitor.State";
    final String ACCESS_RESPONSE_TOPIC = "Access.Response";

    final String DEVICE_REQUEST_TOPIC = "Device.Request";
    final String DEVICE_RESPONSE_TOPIC = "Device.Response";
    final String FAILURE_DATABASE_TOPIC = "Failure.Database";
    //
    final String STATUS_UP_TOPIC  = "Status.UP";
    final String STATUS_DBM_TOPIC = "Status.DBM";
    final String STATUS_REQUEST_UP_TOPIC  = "Status.Request.UP";
    final String STATUS_REQUEST_DBM_TOPIC = "Status.Request.DBM";
    //
    final String COMPONENT_NAME = "UP";
    final Integer ACCESS_NOT_RECEIVED = 4;
    final Object PASS = "PASS";
    final String RequestFailure = "We have access failure";
    final int MAXPIN = 9999;
}
