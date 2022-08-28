package com.dimas519.chargingprotection.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

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
    private SwitchPresenter switchPresenter;
    private LoggingPresenter loggingPresenter;
    private WIFIPresenter wifiPresenter;
    private ServicePresenter servicePresenter;

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


        this.wifiPresenter=new WIFIPresenter(getApplicationContext());
        this.servicePresenter=new ServicePresenter(getApplicationContext());




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        MainServiceThread worker= new MainServiceThread(getBaseContext(),this);

        Thread serviceThread=new Thread(worker);
        serviceThread.start();









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
            if(loggingPresenter.getIP()!=null) {
                Logging.log(msg,
                        loggingPresenter.getIP(),
                        loggingPresenter.getPort(),
                        loggingPresenter.getTimeout());
            }else{
                Notification.showNotification(getApplicationContext(),this.id,"Charging Protection","Set UP logging before logging");
            }
        }
    }

    @Override
    public boolean wifiStatus(String ssid,String switchIP) {
        return WifiChecker.wifiStatus(ssid,switchIP,getApplicationContext());
    }

    @Override
    public SwitchCharger getSwitchCharger() {
        return new SwitchCharger(this.switchPresenter.getIP(),switchPresenter.getPort(),switchPresenter.getTimeout());

    }

    @Override
    public String getSSID() {
        return this.wifiPresenter.getSSID();
    }

    @Override
    public int getLevelOff() {
        return this.servicePresenter.getOffPercentage();
    }

    @Override
    public int getLevelOn() {
        return this.servicePresenter.getONPercentage();
    }

    @Override
    public long getSleepCharging() {
        return this.servicePresenter.getSleepTimeCharging()*1000;
    }

    @Override
    public long getSleepOther() {
        return this.servicePresenter.getSleepTimeOther()*1000;
    }

    @Override
    public boolean getTurnOffService() {
        return this.servicePresenter.getTurnOffStatus();
    }

    @Override
    public boolean getTurnOnService() {
        return this.servicePresenter.getTurnOnStatus();
    }


}
