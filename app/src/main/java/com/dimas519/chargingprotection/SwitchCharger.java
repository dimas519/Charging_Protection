package com.dimas519.chargingprotection;


import com.dimas519.chargingprotection.Service.Connection.ClientConnection;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

public class SwitchCharger {
    private ClientConnection con;
    private String ip;
    private int port;
    //public static String mac="6c2990a99373";

    public SwitchCharger(String ip, int port) {
        this.ip=ip;
        this.port=port;
        this.con = new ClientConnection(ip,port);
    }

    public String getIP(){
        return  this.ip;
    }

    public int getPort(){
        return this.port;
    }



    public boolean turn_off() {
        String feedback;
        try {
            feedback = con.sendToServer("{\"method\":\"setPilot\",\"params\":{\"state\":false}}");
            //expected = {"method":"setPilot","env":"pro","result":{"success":true}}

        } catch (SocketTimeoutException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (Exception e) {

            return false;
        }

        Gson gson = new Gson();
        Map feedbackMap = gson.fromJson(feedback, Map.class);
        Map resMap = (Map) feedbackMap.get("result");
        boolean hasil = (boolean) resMap.get("success");
        return hasil;


    }

    public boolean turn_on(){
        String feedback;
        //expected {"method":"setPilot","env":"pro","result":{"success":true}}
        try{
            feedback = con.sendToServer("{\"method\":\"setPilot\",\"params\":{\"state\":true}}");
            //expected = {"method":"setPilot","env":"pro","result":{"success":true}}

        }catch (SocketTimeoutException e){
            return false;
        }catch (IOException e){
            return false;
        }catch (Exception e){
            return false;
        }

        Gson gson = new Gson();
        Map feedbackMap = gson.fromJson(feedback, Map.class);
        Map resMap = (Map) feedbackMap.get("result");
        boolean hasil  =(boolean) resMap.get("success");
        return hasil;



    }



    private Map getDevicesInfo(){
        String feedback;
        try {
            feedback = con.sendToServer("{\"method\":\"getPilot\",\"params\":{}}");
            //{"method":"getPilot","env":"pro","result":{"mac":"6c2990a99373","rssi":-64,"src":"","state":false,"sceneId":0}}


        }catch (SocketTimeoutException e){
            return null;
        }catch (IOException e){
            return null;
        }catch (Exception e){
            return null;
        }

        Gson gson = new Gson();
        Map feedbackMap = gson.fromJson(feedback, Map.class);
        Map result = (Map) feedbackMap.get("result");
        return result;

    }

    //-1 for null (timeout) 0=false,1=true
    public int getstatus(){
        Map result=getDevicesInfo();
        if(result!=null) {

            return (boolean)result.get("state")? 1:0;
        }else{
            return -1;
        }
    }
}

