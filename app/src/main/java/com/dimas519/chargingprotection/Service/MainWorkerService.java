package com.dimas519.chargingprotection.Service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.Tools.BatteryStatus;
import com.dimas519.chargingprotection.Tools.CODE;
import com.dimas519.chargingprotection.Tools.Waktu;
import com.dimas519.chargingprotection.Widget.Widget_Charging_Protection;

public class MainWorkerService {
    private final Context context;

    private Intent battery;
    private final ServiceInterface si;
    private int error;


    public MainWorkerService(Context context, ServiceInterface si){
        this.context=context;
        this.error=0;
        this.si=si;
    }

    private void error(){
        this.error++;
        if(error==5){
            this.si.error();
            Intent intent = new Intent(context, Widget_Charging_Protection.class);
            intent.setAction(CODE.ChangeStatus);
            intent.putExtra("status", CODE.ERROR);
            context.sendBroadcast(intent);
        }
    }

    public void doMonitor( SwitchCharger switchCharger,long sleepTimeCharging,long sleepTimeOther, int levelCharge,String ssid ){
        IntentFilter iFilter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Thread monitorThread= new Thread(() -> {
            while (true) {
                if (si.wifiStatus(ssid, switchCharger.getIP())) {

                    battery = context.registerReceiver(null, iFilter);
                    int status = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

                    boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING);
                    boolean isFull = (status == BatteryManager.BATTERY_STATUS_FULL);


                    int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);


                    if (isCharging || isFull) {


                        if(level == levelCharge) { //level charge

                            boolean result = switchCharger.turn_off();
                            si.logging(BatteryStatus.getStatus(status) + "level:100, result: " + result + ", waktu phone: " + Waktu.getTimeNow() +
                                    ", ip: " + switchCharger.getIP() + ", getport: " + switchCharger.getPort());
                            if (!result) {
                                error();
                            } else {
                                Intent intent = new Intent(context, Widget_Charging_Protection.class);
                                intent.setAction(CODE.ChangeStatus);
                                intent.putExtra("status", CODE.OFF);
                                context.sendBroadcast(intent);
                            }


                        } else { //not enough percentage
                            si.logging("state:" + BatteryStatus.getStatus(status) + ", level:" + level + ", waktu phone: " + Waktu.getTimeNow() +
                                    ", ip: " + switchCharger.getIP() + ", getport: " + switchCharger.getPort());
                        }
                        sleeping(sleepTimeCharging);


                    } else { //not charge
                        si.logging("not Charging " + level + ", waktu phone: " + Waktu.getTimeNow() +
                                ", ip: " + switchCharger.getIP() + ", getport: " + switchCharger.getPort());
                        sleeping(sleepTimeOther);
                    }


                } else { //not same ssid
                    sleeping(sleepTimeOther);
                }
            }
        });
        monitorThread.start();
    }

    private void sleeping(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            //do nothing because nothing can do
        }
    }

}
