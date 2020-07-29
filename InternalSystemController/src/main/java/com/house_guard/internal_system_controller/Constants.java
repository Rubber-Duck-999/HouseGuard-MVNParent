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
    final String kStateChanged = "UP1";
    final String kIncorrect = "UP2";
    final String kMaxAttempts = "UP3";

    // Camera Monitor
    final String kImageLimit = "CM1";

    //Event Switch Types
    final String kEmailServer = "Email Server Malfunction";
    final String kMotionDetected = "Motion Detected in Flat";


    final String[] kEventTypes = 
        new String[] {kEmailServer, kMotionDetected};
}