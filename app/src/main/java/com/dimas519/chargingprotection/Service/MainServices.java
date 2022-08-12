package com.dimas519.chargingprotection.Service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.dimas519.chargingprotection.Storage.Storage;
import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.Tools.Notification;
import com.dimas519.chargingprotection.Tools.Waktu;


public class MainServices extends Service implements ServiceInterface {
    private final static String id="CP1";
    private final static String serviceName="com.dimas519.chargingprotection.Service.MainServices";

    //sleeptime
    private final int minutesSleep=1;
    private final int secondSleep=0;
    private final int multiplySleep=5;

    //needed
    private SwitchCharger switchPlug;
    private Storage storage;






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
        if(this.storage==null) {
            this.storage = new Storage(getApplicationContext());
        }
        if(switchPlug==null) {
            this.switchPlug = new SwitchCharger(this.storage.getIP(), this.storage.getPort());
        }

        int sleepTime=((this.minutesSleep*60)+this.secondSleep)*1000;

        Logging.log("Service Created, ip: "+switchPlug.getIP()+", getport: "+switchPlug.getPort()+", waktu phone: "+ Waktu.getTimeNow());

        MainWorker2 worker= new MainWorker2(sleepTime,getBaseContext(),this.switchPlug,this);
        worker.doMonitor();
    }







    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logging.log("Service started, ip: "+switchPlug.getIP()+", getport: "+switchPlug.getPort()+", waktu phone: "+ Waktu.getTimeNow());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void error() {
        Notification.showNotification(getBaseContext(),this.id,"Charger Protection","Error 5 times services");
        Logging.log("Error 5 time| Waktu phone:"+Waktu.getTimeNow());
        this.onDestroy();
    }
}
