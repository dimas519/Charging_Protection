package com.dimas519.chargingprotection.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.dimas519.chargingprotection.Presenter.LoggingPresenter;
import com.dimas519.chargingprotection.Presenter.WIFIPresenter;
import com.dimas519.chargingprotection.Presenter.SwitchPresenter;
import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.Tools.Notification;
import com.dimas519.chargingprotection.Tools.Waktu;
import com.dimas519.chargingprotection.Tools.WifiChecker;

public class MainServices extends Service implements ServiceInterface {
    private final static String id="CP1";
    private final static String serviceName="com.dimas519.chargingprotection.Service.MainServices";

    //sleeptime
    private final int minutesSleep=1;
    private final int secondSleep=0;
    private final int multiplySleep=5;

    //needed
    private SwitchCharger switchPlug;
    private SwitchPresenter switchPresenter;
    private WIFIPresenter wifiPresenter;
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

        if(this.wifiPresenter ==null){
            this.wifiPresenter =new WIFIPresenter(getApplicationContext());
        }

        if(this.loggingPresenter==null){
            this.loggingPresenter=new LoggingPresenter(getApplicationContext());
        }


        if(switchPlug==null) {
            this.switchPlug = new SwitchCharger(this.switchPresenter.getIP(), this.switchPresenter.getPort(),this.switchPresenter.getTimeout());
        }

        int sleepTime=((this.minutesSleep*60)+this.secondSleep)*1000;

        logging("Service Created, ip: "+switchPlug.getIP()+", getport: "+switchPlug.getPort()+", waktu phone: "+ Waktu.getTimeNow());

        MainWorkerService worker= new MainWorkerService(sleepTime,getBaseContext(),this);
        worker.doMonitor(this.switchPlug,this.multiplySleep,this.wifiPresenter.getSSID());
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
            Logging.log(msg,loggingPresenter.getIP(),loggingPresenter.getPort(), loggingPresenter.getTimeout());
        }
    }

    @Override
    public boolean wifiStatus(String ssid,String switchIP) {
        return WifiChecker.wifiStatus(ssid,switchIP,getApplicationContext());
    }


}
