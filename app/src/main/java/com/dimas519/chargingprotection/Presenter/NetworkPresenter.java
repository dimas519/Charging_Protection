package com.dimas519.chargingprotection.Presenter;

import android.content.Context;

import com.dimas519.chargingprotection.Storage.Storage;

public class NetworkPresenter {
    private Storage storage;

    public NetworkPresenter(Context context){
        this.storage=new Storage(context);
    }

    public String getSSID(){
        return this.storage.getSSID();
    }

    public void saveSSID(String SSID){
        this.storage.saveSSID(SSID);
    }

    public int getTimeout(){
        return this.storage.getTimeOut();
    }

    public void saveTimeout(int timeout){
        this.storage.saveTimeout(timeout);
    }


}
