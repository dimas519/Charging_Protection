package com.dimas519.chargingprotection.Presenter;

import android.content.Context;

import com.dimas519.chargingprotection.Storage.Storage;

public class LoggingPresenter {
    private Storage storage;

    public LoggingPresenter(Context context){
        this.storage=new Storage(context);
    }

    public void saveIP(String ip){
        this.storage.saveIPLogging(ip);
    }

    public String getIP(){
        return this.storage.getIPLogging();
    }

    public void savePort(int port){
        this.storage.savePortLogging(port);
    }

    public int getPort() {
        return this.storage.getPortLogging();
    }


    public void saveStatus(boolean status){
        this.storage.saveLoggingStatus(status);
    }

    public boolean getStatus(){
        return this.storage.getLoggingStatus();
    }

}
