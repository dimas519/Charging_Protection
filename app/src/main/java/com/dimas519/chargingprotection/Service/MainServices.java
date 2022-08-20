package com.dimas519.chargingprotection.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.dimas519.chargingprotection.Presenter.LoggingPresenter;
import com.dimas519.chargingprotection.Presenter.ServicePresenter;
import com.dimas519.chargingprotection.Presenter.WIFIPresenter;
import com.dimas519.chargingprotection.Presenter.SwitchPresenter;
import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.Tools.Notification;
import com.dimas519.chargingprotection.Tools.Waktu;
import com.dimas519.chargingprotection.Tools.WifiChecker;

public class MainServices extends Service implements ServiceInterface {
    private final String id="CP1";
    private final static String serviceName="com.dimas519.chargingprotection.Service.MainServices";

    //needed
    private SwitchCharger switchPlug;
    private SwitchPresenter switchPresenter;
    private LoggingPresenter loggingPresenter;


    public static String getServiceName(){
        return serviceName;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){

        if(this.switchPresenter==null) {
            this.switchPresenter = new SwitchPresenter(getApplicationContext());
        }

        if(this.loggingPresenter==null){
            this.loggingPresenter=new LoggingPresenter(getApplicationContext());
        }

        WIFIPresenter wifiPresenter=new WIFIPresenter(getApplicationContext());
        ServicePresenter servicePresenter=new ServicePresenter(getApplicationContext());

        if(switchPlug==null) {
            this.switchPlug = new SwitchCharger(
                    this.switchPresenter.getIP(),
                    this.switchPresenter.getPort(),
                    this.switchPresenter.getTimeout());
        }



        logging("Service Created, ip: "+switchPlug.getIP()+ ", getport: "+switchPlug.getPort()+", waktu phone: "+ Waktu.getTimeNow());

        MainWorkerService worker= new MainWorkerService(getBaseContext(),this);
        int turnOffPercentage=servicePresenter.getPercentage()[0].intValue();
        long sleepTimeCharging=servicePresenter.getSleepTimeCharging()*1000;
        long sleepTimeOther=servicePresenter.getSleepTimeOther()*1000;
        worker.doMonitor(this.switchPlug, sleepTimeCharging, sleepTimeOther, turnOffPercentage, wifiPresenter.getSSID());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        logging("Service started, ip: "+switchPlug.getIP()+", getport: "+switchPlug.getPort()+", waktu phone: "+ Waktu.getTimeNow());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void error() {
        Notification.showNotification(getBaseContext(),this.id,"Charger Protection","Error 5 times services");
        logging("Error 5 time| Waktu phone:"+Waktu.getTimeNow());
        this.onDestroy();
    }

    @Override
    public void logging(String msg) { //for check logging features enabled /disabled
        if(this.loggingPresenter.getStatus()){
            Logging.log(msg,
                    loggingPresenter.getIP(),
                    loggingPresenter.getPort(),
                    loggingPresenter.getTimeout());
        }
    }

    @Override
    public boolean wifiStatus(String ssid,String switchIP) {
        return WifiChecker.wifiStatus(ssid,switchIP,getApplicationContext());
    }


}
