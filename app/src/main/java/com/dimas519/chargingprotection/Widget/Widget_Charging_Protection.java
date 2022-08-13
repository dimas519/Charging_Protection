package com.dimas519.chargingprotection.Widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.dimas519.chargingprotection.R;
import com.dimas519.chargingprotection.Storage.Storage;
import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.databinding.WidgetChargingProtectionBinding;


public class Widget_Charging_Protection extends AppWidgetProvider {

    private final String ACTION_UPDATE_CLICK_NEXT = "action.UPDATE_CLICK_NEXT";




    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Intent intent=new Intent(context,this.getClass());
        intent.setAction("ToggleBattery");
        intent.putExtra("ids_size",appWidgetIds.length);

        for (int counter=0; counter<appWidgetIds.length;counter++) {
            intent.putExtra("ids"+counter+1,appWidgetIds[counter]);
        }

        PendingIntent pendingIntent =PendingIntent.getBroadcast(context,1,intent,0);




        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget__charging__protection);
        remoteViews.setOnClickPendingIntent(R.id.widget_Image,pendingIntent);




        remoteViews.setCharSequence(R.id.tvWidget, "setText", "newText");
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
        }


    }





    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);


        if(intent.getAction().equals("ToggleBattery")) {
            int idsSizes = intent.getIntExtra("ids_size",0);
            int ids[]=new int[idsSizes];

            for(int i=0;i<idsSizes;i++){
                ids[i]=intent.getIntExtra("ids"+i+1,0);
            }
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget__charging__protection);

            Storage x=new Storage(context);
            SwitchCharger switchCharger=new SwitchCharger(x.getIP(),x.getPort());

            int currentState =  switchCharger.getstatus();




            boolean res;
        if(currentState==-1){
            Toast.makeText(context, "Switch Error", Toast.LENGTH_SHORT).show();
        }else if(currentState==1){
            res=switchCharger.turn_off();

            if(res){
                Toast.makeText(context, "Switch Turned Off", Toast.LENGTH_SHORT).show();
                remoteViews.setTextViewText(R.id.tvWidget, "Turn On");

            }else{
                Toast.makeText(context, "Switch To Failed Turn Off", Toast.LENGTH_SHORT).show();
                remoteViews.setTextViewText(R.id.tvWidget, "Error");
            }


        }else{
            res=switchCharger.turn_on();
            if(res){
                Toast.makeText(context, "Switch Turned On", Toast.LENGTH_SHORT).show();
                remoteViews.setTextViewText(R.id.tvWidget, "Turn Off");

            }else{
                Toast.makeText(context, "Switch To Failed Turn On", Toast.LENGTH_SHORT).show();
                remoteViews.setTextViewText(R.id.tvWidget, "Error");
            }
        }





            for (int appWidgetId : ids ){
                AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId,remoteViews);
            }
        }
    }




}