package com.house_guard.internal_system_controller;

public interface Constants {

    // Fault Handler
    final String kEmail = "FH1";
    final String kMotion = "FH2";
    
    // System Processor
    final String kFaultDead = "SYP1";
    final String kCPUHigh = "SYP2";

    // Environment Manager
    final String kLostConnection = "EVM1";
    final String kWeatherApi = "EVM2";
    final String kWeatherReading = "EVM3";

    // Network Access Controller
    final String kNmap = "NAC1";
    final String kBlocked = "NAC2";
    final String kUnknown = "NAC3";

    // User Panel
    final String StateChanged = "UP1";
    final String Incorrect = "UP2";
    final String MaxAttempts = "UP3";

    // Camera Monitor
    final String ImageLimit = "CM1";

    final String[] kEventTypes = 
        new String[] {"Email Server Malfunction",
                      "Motion Detected in Flat"};
}