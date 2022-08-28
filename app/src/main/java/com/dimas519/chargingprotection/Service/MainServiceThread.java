package com.dimas519.chargingprotection.Service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.dimas519.chargingprotection.SwitchCharger;

import com.dimas519.chargingprotection.Tools.CODE;
import com.dimas519.chargingprotection.Tools.Waktu;
import com.dimas519.chargingprotection.Widget.Widget_Charging_Protection;

public class MainServiceThread implements Runnable {
    private final Context context;

    private Intent battery;
    private final ServiceInterface si;
    private int error;
    private SwitchCharger switchCharger;


    public MainServiceThread(Context context, ServiceInterface si){
        this.context=context;
        this.error=0;
        this.si=si;
    }


    @Override
    public void run() {
        IntentFilter iFilter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        this.switchCharger=this.si.getSwitchCharger();
        String ssid=this.si.getSSID();
        int levelOFF=this.si.getLevelOff();
        int levelON=this.si.getLevelOn();
        long sleepTimeCharging=this.si.getSleepCharging();
        long sleepTimeOther=this.si.getSleepOther();

        while (true) {
            if (si.wifiStatus(ssid, switchCharger.getIP())) {

                battery = context.registerReceiver(null, iFilter);
                int status = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

                boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING);
                boolean isFull = (status == BatteryManager.BATTERY_STATUS_FULL);

                if (isCharging || isFull) {

                    if(this.si.getTurnOffService()){
                        doChargingProcedure(levelOFF, sleepTimeCharging);
                    }

                    sleeping(sleepTimeCharging);
                } else { //not charge

                    if(this.si.getTurnOnService()) {
                        doDischargingProcedure(levelON, sleepTimeOther);
                    }

                    sleeping(sleepTimeOther);
                }
            } else { //not same ssid
                sleeping(sleepTimeOther);
            }
        }
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


    private void doChargingProcedure(int levelCharge, long sleepTimeCharging){

        int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        if(level >= levelCharge) { //level charge
            System.out.println("charging"+(level>=levelCharge));
            int switchCurrentState=switchCharger.getstatus();

            if(switchCurrentState== CODE.ON ){
                boolean result = this.switchCharger.turn_off();


                si.logging("Action: Charging, level:100, result: " + result + ", waktu phone: " + Waktu.getTimeNow() +
                        ", ip: " + this.switchCharger.getIP() + ", getport: " + this.switchCharger.getPort());
                if (!result) {
                    error();
                } else {
                    Intent intent = new Intent(context, Widget_Charging_Protection.class);
                    intent.setAction(CODE.ChangeStatus);
                    intent.putExtra("status", CODE.OFF);
                    context.sendBroadcast(intent);
                }
            }

        } else { //not enough percentage
            si.logging("Action: Charging  level:" + level + ", waktu phone: " + Waktu.getTimeNow() +
                    ", ip: " + this.switchCharger.getIP() + ", getport: " + this.switchCharger.getPort());
        }
    }


    private void doDischargingProcedure(int levelCharge,long sleepTimeOther){

        int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        if(level<=levelCharge){
            System.out.println("discharging"+(level>=levelCharge));
            int switchCurrentState=switchCharger.getstatus();

            if(switchCurrentState==CODE.OFF) {
                boolean result = this.switchCharger.turn_on();
                si.logging( "Action: Charging, level:"+level+", result: " + result + ", waktu phone: " + Waktu.getTimeNow() +
                        ", ip: " + this.switchCharger.getIP() + ", getport: " + this.switchCharger.getPort());
                if (!result) {
                    error();
                } else {
                    Intent intent = new Intent(context, Widget_Charging_Protection.class);
                    intent.setAction(CODE.ChangeStatus);
                    intent.putExtra("status", CODE.OFF);
                    context.sendBroadcast(intent);
                }
            }


        }else{
            si.logging("not Charging " + level + ", waktu phone: " + Waktu.getTimeNow() +
                    ", ip: " + switchCharger.getIP() + ", getport: " + switchCharger.getPort());

        }
    }




    
    private void sleeping(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            //do nothing because nothing can do
        }
    }


}
