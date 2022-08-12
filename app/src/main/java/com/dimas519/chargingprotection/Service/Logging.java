package com.dimas519.chargingprotection.Service;



import android.util.Log;

import com.dimas519.chargingprotection.Service.Connection.ClientConnection;
import com.dimas519.chargingprotection.Tools.Notification;

import java.io.IOException;
import java.net.SocketTimeoutException;


public class Logging {
    private static boolean logging=true;

    public static void log(String msg) {
        Thread log=new Thread(new Runnable() {
            @Override
            public void run() {
                ClientConnection con = new ClientConnection("192.168.100.100",9162);
                try {
                    con.sendToServerWithoutResponse(msg);
                }catch (SocketTimeoutException e){
                    Log.d("konci2", "run: to");
                }catch (IOException e){
                    Log.d("konci2", "run: IOE");
                }catch (Exception e){
                    
                }
            }
        });

        if(logging) {
            log.start();
        }
}



}
