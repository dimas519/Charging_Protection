package com.dimas519.chargingprotection.Service;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.dimas519.chargingprotection.Storage.Storage;
import com.dimas519.chargingprotection.SwitchCharger;



//digunakan untuk observer atau  sebagai thread Lain, karena untuk mengecek status di switch
//-1 untuk time out
//0 untuk false
//1 untuk true




public class OneTimeServices extends Worker {
    Data parameter;
    private SwitchCharger switchCharger;

    public OneTimeServices(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.parameter=workerParams.getInputData();

        Storage storage=new Storage(context);


        this.switchCharger=new SwitchCharger(storage.getIP(),storage.getSwitchPort());
    }

    @NonNull
    @Override
    public Result doWork() {
        int kode=this.parameter.getInt("code",-1);
        if (kode ==1) {
            int result =  this.switchCharger.getstatus();
            Data output = new Data.Builder()
                    .putInt("hasil", result)
                    .build();
            return Result.success(output);
        }else if(kode==2) {
            boolean conditionToggle=this.parameter.getBoolean("status",false);

            boolean hasil;
            if(conditionToggle){
                hasil=this.switchCharger.turn_off();
            }else{
                hasil=this.switchCharger.turn_on();
            }

            int res=hasil?
                conditionToggle?0:1 :-1;

            Data output = new Data.Builder()
                    .putInt("hasil", res)
                    .build();

            return Result.success(output);
        }else {
            Data output = new Data.Builder()
                    .putString("error", "error code")
                    .build();
            return Result.failure(output);
        }
    }
}
