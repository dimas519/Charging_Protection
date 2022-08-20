package com.dimas519.chargingprotection.Presenter;

import android.content.Context;


public class WIFIPresenter extends NetworkPresenter{

    public WIFIPresenter(Context context){
        super(context);
    }


    public String getSSID(){
        return this.storage.getSSID();
    }

    public void saveSSID(String SSID){
        this.storage.saveSSID(SSID);
    }

}
