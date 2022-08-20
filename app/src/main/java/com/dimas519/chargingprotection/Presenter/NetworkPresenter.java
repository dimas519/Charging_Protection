package com.dimas519.chargingprotection.Presenter;

import android.content.Context;

import com.dimas519.chargingprotection.Storage.Storage;

public class NetworkPresenter {

    protected Storage storage;

    public NetworkPresenter(Context context){
        this.storage=new Storage(context);
    }

    public int getTimeout(){
        return this.storage.getTimeOut();
    }

    public void saveTimeout(int timeout){
        this.storage.saveTimeout(timeout);
    }
}
