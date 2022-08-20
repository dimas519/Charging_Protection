package com.dimas519.chargingprotection.Presenter;

import android.content.Context;

import com.dimas519.chargingprotection.Storage.Storage;

public class WifiPresenter extends NetworkPresenter{

    public WifiPresenter(Context context){
        super(context);
    }


    public String getSSID(){
        return this.storage.getSSID();
    }

    public void saveSSID(String SSID){
        this.storage.saveSSID(SSID);
    }

}
