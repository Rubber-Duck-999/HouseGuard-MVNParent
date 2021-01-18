package com.house_guard.user_panel;

import com.house_guard.user_panel.DeviceRequest;

public interface EventListener {
    void onEventDevice(DeviceRequest device);

    void onEventStatus();
}