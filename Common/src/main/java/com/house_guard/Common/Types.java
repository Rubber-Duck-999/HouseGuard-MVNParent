package com.house_guard.Common;

public interface Types
{
    public enum Actions
    {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        CLEAR,
        ZERO,
        BACK,
        ENTER
    }

    public enum State
    {
        ON,
        OFF
    }

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

    final int RESET = 0;
    final String OFF = "OFF";
    final String ON = "ON";
    final Integer MAX_ATTEMPTS = 3;

    final int ATTEMPTS_MAXED = 0;
    final int INCORRECT = 1;
    final int CORRECT = 2;
    final int TIMEOUT = 3;

    // Device.Update.State choices
    final String ADD_STATE = "POST";
    final String EDIT_STATE = "PATCH";
    final String REMOVE_STATE = "DELETE";
    //
    final String REQUEST_ACCESS_TOPIC = "Request.Access";
    final String MONITOR_STATE_TOPIC = "Monitor.State";
    final String DEVICE_UPDATE_TOPIC = "Device.Update";
    final String DEVICE_REQUEST_TOPIC = "Device.Request";
    final String ACCESS_RESPONSE_TOPIC = "Access.Response";
    final String DEVICE_RESPONSE_TOPIC = "Device.Response";

    final String EVENT_TOPIC_ALL = "Event.*";
    final String EVENT_TOPIC     = "Event.";
    final String EVENT_TOPIC_UP  = "Event.UP";
    final String EVENT_TOPIC_SYP = "Event.SYP";
    final String EVENT_TOPIC_DBM = "Event.DBM";
    final String EVENT_TOPIC_NAC = "Event.NAC";
    final String EVENT_TOPIC_EVM = "Event.EVM";
    final String EVENT_TOPIC_FH  = "Event.FH";
    final String DATA_INFO_TOPIC = "Data.Info";

    final String REQUEST_DATABASE_TOPIC = "Request.Database";
    final String FAILURE_DATABASE_TOPIC = "Failure.Database";
    //
    final String STATUS_UP_TOPIC  = "Status.UP";
    final String STATUS_DBM_TOPIC = "Status.DBM";
    final String STATUS_REQUEST_UP_TOPIC  = "Status.Request.UP";
    final String STATUS_REQUEST_DBM_TOPIC = "Status.Request.DBM";

    final String COMPONENT_NAME = "UP";
    final Integer ACCESS_NOT_RECEIVED = 4;
    final Object PASS = "PASS";
    final String RequestFailure = "We have access failure";
    final int MAXPIN = 9999;
}
