package com.dimas519.chargingprotection.Service;

import com.dimas519.chargingprotection.SwitchCharger;

public interface ServiceInterface {
    void error();
    void logging(String msg);
    boolean wifiStatus(String ssid,String ipAddress);
    SwitchCharger getSwitchCharger();
    String getSSID();
    int getLevelOff();
    int getLevelOn();
    long getSleepCharging();
    long getSleepOther();
    boolean getTurnOffService();
    boolean getTurnOnService();
}
