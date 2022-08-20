package com.dimas519.chargingprotection.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiChecker {
    public static boolean wifiStatus(String ssid,String switchIP,Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        WifiInfo info = wifiManager.getConnectionInfo();


        if (info!=null){
            String ssidConnected  = info.getSSID();
            if(ssidConnected.equalsIgnoreCase("\""+ssid+"\"")){
                int ip=info.getIpAddress();
                @SuppressLint("DefaultLocale") String currIP = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
                String[] currIPArr=currIP.split("\\.");
                String[] switchIPArr=switchIP.split("\\.");

                if(!(currIPArr.length==0 && switchIPArr.length==0)    //check is the array empty or not
                        && currIPArr[0].equals(switchIPArr[0])  &&  //check first hex
                        currIPArr[1].equals(switchIPArr[1]) &&  //check second hex
                        currIPArr[2].equals(switchIPArr[2])){ //check third hex
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
