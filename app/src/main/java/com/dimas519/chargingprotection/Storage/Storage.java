package com.dimas519.chargingprotection.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Storage {
    private SharedPreferences sp;
    private final String address="ip";
    private final String port="port";
    private final String mac="mac";
    private final String status="status";

    public Storage(Context c){
        this. sp= PreferenceManager.getDefaultSharedPreferences(c);
    }

    public void saveIP(String ip){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putString(this.address,ip);
        editor.apply();
    }

    public String getIP(){
        return sp.getString(this.address,"192.168.100.17");
    }

    public void setPort(int port){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putInt(this.port,port);
        editor.apply();
    }

    public int getPort(){
        return sp.getInt(this.port,38899);
    }

    public void setLoggingStatus(boolean status){
        SharedPreferences.Editor editor= this.sp.edit();
        editor.putBoolean(this.status,status);
        editor.apply();
    }

    public boolean getLoggingStatus(){
        return sp.getBoolean(this.status,false);
    }







}



