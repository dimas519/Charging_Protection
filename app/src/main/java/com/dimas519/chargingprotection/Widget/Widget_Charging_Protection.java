package com.dimas519.chargingprotection.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.dimas519.chargingprotection.R;
import com.dimas519.chargingprotection.Storage.Storage;
import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.Widget.Thread.OwnHandler;
import com.dimas519.chargingprotection.Widget.Thread.WidgetThread;


public class Widget_Charging_Protection extends AppWidgetProvider implements WidgetInterface {



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget__charging__protection);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
        }


    }





    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(action.equals(WidgetCode.ChangeStatus)){
            int status=intent.getIntExtra("status",-1);

            if(status==WidgetCode.ON) {
                responseFromThread(context, WidgetCode.SuccessTurnOn);
            }else if(status==WidgetCode.OFF){
                responseFromThread(context, WidgetCode.SuccessTurnOff);
            }else if(status==WidgetCode.ERROR){
                responseFromThread(context, WidgetCode.SwitchError);
            }


        }else{
            Storage x=new Storage(context);
            SwitchCharger switchCharger=new SwitchCharger(x.getIP(),x.getPort());

            OwnHandler ownHandler =new OwnHandler(context,this);
            WidgetThread widgetThread=new WidgetThread(ownHandler, switchCharger);
            if(action.equals(WidgetCode.ToggleAction)) {
                widgetThread.setAction(WidgetCode.ToggleAction);;
            }
            Thread otherThread=new Thread(widgetThread);
            otherThread.start();
        }


    }


    @Override
    public void responseFromThread(Context context, int result) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget__charging__protection);

        if(result==WidgetCode.SuccessTurnOn){
            remoteViews.setTextViewText(R.id.tvWidget, "Turn Off");
            remoteViews.setInt(R.id.widget_button, "setImageResource",R.mipmap.battery_logo_round);
            Toast.makeText(context, "Success Turn On", Toast.LENGTH_SHORT).show();
        }else if(result==WidgetCode.SuccessTurnOff){
            remoteViews.setTextViewText(R.id.tvWidget, "Turn On");
            remoteViews.setInt(R.id.widget_button, "setImageResource",R.mipmap.battery_logo_off_round);
            Toast.makeText(context, "Success Turn Off", Toast.LENGTH_SHORT).show();
        }else if(result==WidgetCode.FailedTurnOff){
            remoteViews.setTextViewText(R.id.tvWidget, "Turn Off");
            remoteViews.setInt(R.id.widget_button, "setImageResource",R.mipmap.battery_logo_off_round);
            Toast.makeText(context, "Failed Turn Off", Toast.LENGTH_SHORT).show();
        }else if(result==WidgetCode.FailedTurnOn){
            remoteViews.setTextViewText(R.id.tvWidget, "Turn On Error");
            remoteViews.setInt(R.id.widget_button, "setImageResource",R.mipmap.battery_logo_round);
            Toast.makeText(context, "Failed Turn On", Toast.LENGTH_SHORT).show();
        }else if(result==WidgetCode.SwitchError){
            remoteViews.setTextViewText(R.id.tvWidget, "Status Error");
            Toast.makeText(context, "Status Error", Toast.LENGTH_SHORT).show();
        }else if(result==WidgetCode.ON){
            remoteViews.setTextViewText(R.id.tvWidget, "Turn Off");
            remoteViews.setInt(R.id.widget_button, "setImageResource",R.mipmap.battery_logo_round);

        }else if(result==WidgetCode.OFF){
            remoteViews.setTextViewText(R.id.tvWidget, "Turn On");
            remoteViews.setInt(R.id.widget_button, "setImageResource",R.mipmap.battery_logo_off_round);
        }



        Intent intent=new Intent(context,this.getClass());
        intent.setAction(WidgetCode.ToggleAction);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(context,1,intent,0);

        remoteViews.setOnClickPendingIntent(R.id.widget_button,pendingIntent);



        int[] appWidgetIds= AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context.getPackageName(), this.getClass().getName()));
        for (int appWidgetId : appWidgetIds) {
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
        }


    }

}