package com.dimas519.chargingprotection.Service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.Tools.BatteryStatus;
import com.dimas519.chargingprotection.Tools.Waktu;
import com.dimas519.chargingprotection.Widget.WidgetCode;
import com.dimas519.chargingprotection.Widget.Widget_Charging_Protection;

public class MainWorkerService {
    private final String id="CP2";
    private  int sleepTimeCharging;
    private int sleepTimeNotCharing;


    private Context context;
    private IntentFilter ifilter;
    private Intent battery;


    private SwitchCharger switchCharger;
    private ServiceInterface si;


    private int error;
    private int levelCharge=100;

    public MainWorkerService(int sleepTime, int multiply, Context context, SwitchCharger switchCharger, ServiceInterface si){
        this.sleepTimeCharging=sleepTime;
        this.sleepTimeNotCharing=sleepTime*multiply;
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
            Intent intent = new Intent(context, Widget_Charging_Protection.class);
            intent.setAction(WidgetCode.ChangeStatus);
            intent.putExtra("status", WidgetCode.ERROR);
            context.sendBroadcast(intent);
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



                        if (level == levelCharge) { //level charge

                            boolean result=switchCharger.turn_off();
                            si.logging(BatteryStatus.getStatus(status)+"level:100, result: "+result+", waktu phone: "+ Waktu.getTimeNow() +
                                    ", ip: "+switchCharger.getIP()+", getport: "+switchCharger.getPort());
                            if(!result){
                               error();
                            }else{
                                Intent intent = new Intent(context, Widget_Charging_Protection.class);
                                intent.setAction(WidgetCode.ChangeStatus);
                                intent.putExtra("status", WidgetCode.OFF);
                                context.sendBroadcast(intent);
                            }


                            sleeping(sleepTimeCharging);

                        } else { //not enough percentage
                            si.logging("state:"+ BatteryStatus.getStatus(status) +", level:"+level+", waktu phone: "+Waktu.getTimeNow()+
                                    ", ip: "+switchCharger.getIP()+", getport: "+switchCharger.getPort());
                            sleeping(sleepTimeCharging);
                        }


                    } else { //not charge
                        si.logging("not Charging "+level+", waktu phone: "+ Waktu.getTimeNow()+
                                ", ip: "+switchCharger.getIP()+", getport: "+switchCharger.getPort());
                        sleeping(sleepTimeNotCharing);
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
