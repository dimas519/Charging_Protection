package com.dimas519.chargingprotection.Presenter;

import android.content.Context;


public class SwitchPresenter  extends NetworkPresenter{

    public SwitchPresenter(Context context){
        super(context);
    }

    public void saveIP(String ip){
        this.storage.saveIP(ip);
    }

    public String getIP() {
        return storage.getIP();
    }

    public void savePort(int port){
        this.storage.saveSwitchPort(port);
    }

    public int getPort(){
        return this.storage.getSwitchPort();
    }


}
