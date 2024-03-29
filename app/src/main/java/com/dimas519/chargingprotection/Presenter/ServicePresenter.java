package com.dimas519.chargingprotection.Presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.dimas519.chargingprotection.Storage.Storage;

import java.util.List;

public class ServicePresenter{
    private final Storage storage;
    public ServicePresenter(Context context) {
        this.storage=new Storage(context);
    }

    public void setSleepTimeCharging(Long sleepTime){
        this.storage.saveSleepTimeCharging(sleepTime);
    }

    public void setSleepTimeOther(Long sleepTime){
        this.storage.saveSleepTimeOther(sleepTime);
    }

    public void setPercentage(List<Float> sliderValue){
        if(sliderValue.get(0) >sliderValue.get(1)){
            this.storage.saveTurnOffPercentage(sliderValue.get(0));
            this.storage.saveTurnOnPercentage(sliderValue.get(1));
        }else{
            this.storage.saveTurnOffPercentage(sliderValue.get(1));
            this.storage.saveTurnOnPercentage(sliderValue.get(0));
        }
    }

    public long getSleepTimeCharging(){
        return this.storage.getSleepTimeCharging();
    }

    public long getSleepTimeOther(){
        return this.storage.getSleepTimeOther();
    }


    public Float[] getPercentage(){
        float off= this.storage.getTurnOffPercentage();
        float on= this.storage.getTurnOnPercentage();

        return new Float[]{on,off};
    }

    public int getOffPercentage(){
            return (int)this.storage.getTurnOffPercentage();
    }

    public int getONPercentage(){
        return (int)this.storage.getTurnOnPercentage();
    }

    public void setTurnOnStatus(boolean status){
        this.storage.saveTurnOnStatus(status);
    }

    public boolean getTurnOnStatus(){
        return this.storage.getTurnOnStatus();
    }

    public void setTurnOffStatus(boolean status){
        this.storage.saveTurnOffStatus(status);
    }

    public boolean getTurnOffStatus(){
        return this.storage.getTurnOffStatus();
    }



}
