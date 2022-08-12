package com.dimas519.chargingprotection.Service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;


import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.Tools.BatteryStatus;
import com.dimas519.chargingprotection.Tools.Notification;
import com.dimas519.chargingprotection.Tools.Waktu;

public class MainWorker2 {
    private final String id="CP2";
    private  int sleepTime;



    private Context context;
    private IntentFilter ifilter;
    private Intent battery;



    private SwitchCharger switchCharger;
    private ServiceInterface si;


    private int error;

    public MainWorker2(int sleepTime,Context context,SwitchCharger switchCharger,ServiceInterface si){
        this.sleepTime=sleepTime;
        this.context=context;
        this.ifilter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.switchCharger=switchCharger;
        this.error=0;
        this.si=si;
    }

    private void error(){
        this.error++;
        if(error==5){
            this.si.error();
        }
    }

    public void doMonitor(){
        Thread monitorThread= new Thread() {
            public void run() {
                while (true) {
                    battery = context.registerReceiver(null, ifilter);
                    int status = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

                    boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING);
                    boolean isFull = (status == BatteryManager.BATTERY_STATUS_FULL);


                    int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);



                    if (isCharging ||isFull){
                        if (level == 100) {

                            sleeping(2*60*1000);

                            boolean result=switchCharger.turn_off();


                            Logging.log(BatteryStatus.getStatus(status)+"level:100, result: "+result+", waktu phone: "+ Waktu.getTimeNow() +
                                    ", ip: "+switchCharger.getIP()+", getport: "+switchCharger.getPort());
                            if(!result){
                               error();
                            }


                            sleeping(sleepTime);

                        } else {
                            String message ="state:"+ BatteryStatus.getStatus(status) +", level:"+level+", waktu phone: "+Waktu.getTimeNow()+
                                    ", ip: "+switchCharger.getIP()+", getport: "+switchCharger.getPort();
                            Logging.log(message);

                            sleeping(sleepTime);
                        }
                    } else {
                        Logging.log("not Charging "+level+", waktu phone: "+ Waktu.getTimeNow()+
                                ", ip: "+switchCharger.getIP()+", getport: "+switchCharger.getPort());
                        sleeping(sleepTime);
                    }



                }





            }
        };

        monitorThread.start();


    }

    private void sleeping(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
