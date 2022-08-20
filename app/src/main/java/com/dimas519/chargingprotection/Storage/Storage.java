package com.dimas519.chargingprotection.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Storage {
    private final SharedPreferences sp;

    //switch
    private final String switchAddress ="ip";
    private final String switchPort ="port";


    //logging
    private final String loggingAddress ="Logging_ip";
    private final String loggingPort ="Logging_port";
    private final String loggingStatus="Logging_status";


    //wifi conf
    private final String WIFI_name="SSID";
    private final String TIMEOUT="TIMEOUT(MS)";


    //service  --sleep
    private final String sleepTimeCharging="sleepTimeCharging";
    private final String sleepTimeOther="sleepTimeNotCharging";
    private final String turnOffPercentage="turnOFF";
    private final String turnOnPercentage="turnON";

    public Storage(Context c){
        this. sp= PreferenceManager.getDefaultSharedPreferences(c);
    }

    // ---------------------------------------------------------------------
    // SWITCH
    //----------------------------------------------------------------------

    public void saveIP(String ip){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putString(this.switchAddress,ip);
        editor.apply();
    }

    public String getIP(){
        return sp.getString(this.switchAddress,null);
    }

    public void saveSwitchPort(int switchPort){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putInt(this.switchPort, switchPort);
        editor.apply();
    }

    public int getSwitchPort(){
        return sp.getInt(this.switchPort,38899);
    }

    // ---------------------------------------------------------------------
    // LOGGING
    //----------------------------------------------------------------------

    public void saveIPLogging(String ip){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putString(this.loggingAddress,ip);
        editor.apply();
    }

    public String getIPLogging(){
        return sp.getString(this.loggingAddress,null);
    }

    public void savePortLogging(int switchPort){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putInt(this.loggingPort, switchPort);
        editor.apply();
    }

    public int getPortLogging() {
        return sp.getInt(this.loggingPort, -1);
    }


    public void saveLoggingStatus(boolean status){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putBoolean(this.loggingStatus,status);
        editor.apply();
    }

    public boolean getLoggingStatus(){
        return sp.getBoolean(this.loggingStatus,false);
    }

    // ---------------------------------------------------------------------
    // WIFI CONF OR NETWORK CONF
    //----------------------------------------------------------------------

    public void saveSSID(String SSID){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putString(this.WIFI_name,SSID);
        editor.apply();
    }

    public String getSSID(){
        return sp.getString(this.WIFI_name,null);
    }

    public void saveTimeout(int timeout){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putInt(this.TIMEOUT, timeout);
        editor.apply();
    }

    public int getTimeOut() {
        return sp.getInt(this.TIMEOUT, 5000);
    }

    // ---------------------------------------------------------------------
    // Service -- sleep time
    //----------------------------------------------------------------------

    public void saveSleepTimeCharging(Long sleepTime){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putLong(this.sleepTimeCharging, sleepTime);
        editor.apply();
    }

    public Long getSleepTimeCharging(){
        return sp.getLong(this.sleepTimeCharging, -1);
    }

    public void saveSleepTimeOther(Long sleepTime){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putLong(this.sleepTimeOther, sleepTime);
        editor.apply();
    }

    public Long getSleepTimeOther(){
        return sp.getLong(this.sleepTimeOther, -1);
    }

    public void saveTurnOffPercentage(float percentage){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putFloat(this.turnOffPercentage, percentage);
        editor.apply();
    }

    public float getTurnOffPercentage(){
        return sp.getFloat(this.turnOffPercentage,100);
    }

    public void saveTurnOnPercentage(float percentage){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putFloat(this.turnOnPercentage, percentage);
        editor.apply();
    }

    public float getTurnOnPercentage(){
        return sp.getFloat(this.turnOnPercentage,15);
    }

}



