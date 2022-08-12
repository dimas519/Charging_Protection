package com.dimas519.chargingprotection.Tools;

import android.os.BatteryManager;

public class BatteryStatus {
    public static String getStatus(int status){
        if(status== BatteryManager.BATTERY_STATUS_FULL){
            return "Battery Full";
        }else if(status==BatteryManager.BATTERY_STATUS_CHARGING){
            return "Charging";
        }else if(status==BatteryManager.BATTERY_STATUS_DISCHARGING) {
            return "Discharging";
        }else if (status==BatteryManager.BATTERY_STATUS_NOT_CHARGING){
            return "not Charging-not enough power";
        }else if(status==BatteryManager.BATTERY_STATUS_UNKNOWN){
            return "unknown";
        }else {
            return "error code";
        }
    }



}
