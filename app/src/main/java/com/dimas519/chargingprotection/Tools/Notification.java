package com.dimas519.chargingprotection.Tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.dimas519.chargingprotection.MainActivity;
import com.dimas519.chargingprotection.R;
import java.util.ArrayList;

public class Notification {
    private static NotificationManager notificationManager;
    private final static ArrayList<String> id=new ArrayList<String>();

    public static void showNotification(Context context, String channelId, String title, String content,boolean onGoing) {
        int id=getId(channelId);

        Intent intent=new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.mipmap.battery_logo_foreground)
            .setContentTitle(title)
            .setContentText(content)
                .setOngoing(onGoing)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel=new NotificationChannel(channelId,"Charger Protection", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(id,builder.build());

    }

    public static void showNotification(Context context, String channelId ,String title , String content) {
        showNotification(context, channelId,title,content,false);
    }


    public static void clearNotification(String channelId){
        int id=getId(channelId);
        notificationManager.cancel(id);
    }

    public static int getId(String channels){
        int i=0;
        for (String curr: id){
            if(curr .equals(channels)){
                return i;
            }
            i++;
        }
        return i;
    }


}
