package com.dimas519.chargingprotection.Widget.Thread;

import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.Tools.CODE;

public class WidgetThread implements Runnable {
    private OwnHandler handler;
    private SwitchCharger switchCharger;
    private String action;

    public WidgetThread(OwnHandler h,SwitchCharger switchCharger){
        this.handler=h;
        this.switchCharger=switchCharger;
    }
    private final void setName (String name){
        Thread.currentThread().setName(name);
    }

    public void setAction(String action){
        this.action=action;
    }

    @Override
    public void run() {
        this.setName("Widget Thread");

        int currentState = switchCharger.getstatus();
        if(action==null){
            if(currentState==-1) {
                this.handler.messageProcess(CODE.SwitchError);
            }else if(currentState==1){
                this.handler.messageProcess(CODE.ON);
            }else{
                this.handler.messageProcess(CODE.OFF);
            }
        } else if(action.equals(CODE.ToggleAction)) {
            this.toogleSwitch(currentState);
        }


    }

    private void toogleSwitch(int currentState){
        boolean res;
        if(currentState==-1){
            this.handler.messageProcess(CODE.SwitchError);
        }else if(currentState==1){ //kalau kondisinya nyala
            res=switchCharger.turn_off(); //maka matikan

            if(res){ //check berhasil atau tidak
                this.handler.messageProcess(CODE.SuccessTurnOff);
            }else{
                this.handler.messageProcess(CODE.FailedTurnOff);
            }


        }else{
            res=switchCharger.turn_on();
            if(res){
                this.handler.messageProcess(CODE.SuccessTurnOn);
            }else{
                this.handler.messageProcess(CODE.FailedTurnOn);
            }
        }
    }


}
