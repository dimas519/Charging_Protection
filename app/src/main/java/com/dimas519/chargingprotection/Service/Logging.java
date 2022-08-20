package com.dimas519.chargingprotection.Service;

import com.dimas519.chargingprotection.Service.Connection.ClientConnection;
import java.io.IOException;
import java.net.SocketTimeoutException;


public class Logging {

    public static void log(String msg,String ip,int port,int timeout) {
        Thread log=new Thread(new Runnable() {
            @Override
            public void run() {
                ClientConnection con = new ClientConnection(ip, port, timeout);
                try {
                    con.sendToServer(msg);
                }catch (SocketTimeoutException e){
                    //do Something socket exception
                }catch (IOException e){
                    //do Something IO exception
                }catch (Exception e){
                    
                }
            }
        });
        log.start();
    }



}
