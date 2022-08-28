package com.dimas519.chargingprotection.Tools;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dimas519.chargingprotection.MainActivity;
import com.dimas519.chargingprotection.Service.MainServices;

public class ServiceHelper {
    public static boolean isServiceRunning(@NonNull Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(100)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void toggleService(@NonNull Context context, String serviceName, Class service){
        if(ServiceHelper.isServiceRunning(context, serviceName)){
            context.stopService(new Intent( context,service ));
            Toast.makeText(context, "Service Stopped", Toast.LENGTH_LONG).show();
        }else{
            context.startService(new Intent( context, service));
            Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show();
        }
    }

    public static void restartService(@NonNull Context context, String serviceName, Class service){
        if(ServiceHelper.isServiceRunning(context, serviceName)){
            context.stopService(new Intent(context,service));
            context.startService(new Intent( context, service));
            Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show();
        }
    }





}
