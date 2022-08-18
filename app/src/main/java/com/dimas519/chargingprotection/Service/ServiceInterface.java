package com.dimas519.chargingprotection.Service;

public interface ServiceInterface {
    void error();
    void logging(String msg);
    boolean wifiStatus(String ssid,String ipAddress);

}
