package com.dimas519.chargingprotection.Presenter;

import android.content.Context;

import java.util.List;

public class ServicePresenter extends SwitchPresenter{
    public ServicePresenter(Context context) {
        super(context);
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

}
