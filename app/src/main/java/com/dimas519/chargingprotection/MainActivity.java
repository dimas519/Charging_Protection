package com.dimas519.chargingprotection;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;


import com.dimas519.chargingprotection.Service.Logging;
import com.dimas519.chargingprotection.Service.MainServices;
import com.dimas519.chargingprotection.Service.OneTimeServices;
import com.dimas519.chargingprotection.Service.BroadcastReceiverSwitch;
import com.dimas519.chargingprotection.Storage.Storage;
import com.dimas519.chargingprotection.Tools.BatteryStatus;
import com.dimas519.chargingprotection.databinding.ActivityMainBinding;

import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    private WorkManager wm;
    private Storage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //init-int
        this.binding =ActivityMainBinding.inflate(getLayoutInflater());
        this.wm=WorkManager.getInstance(getApplicationContext());
        this.storage= new Storage(getApplicationContext());

        String address=this.storage.getIP();
        int port= this.storage.getPort();
        this.setDeviceInfo(address,port);



        //binding setting
        initCheckSwitchStatus();

        this.binding.service.setChecked(isServiceRunning(MainServices.getServiceName()));




        //on click listener
        this.binding.switch3.setOnClickListener(this);
        this.binding.service.setOnClickListener(this);
        this.binding.save.setOnClickListener(this);




        //check condition battery
        IntentFilter ifilter= new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent battery = getBaseContext().registerReceiver(null, ifilter);
        int status = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        this.binding.state.setText(BatteryStatus.getStatus(status));

        setContentView(binding.getRoot());
    }


    private void setDeviceInfo(String ip,int port){
        this.binding.ipAdress.setText(ip);
        this.binding.port.setText(port+"");

        this.storage.setPort(port);
        this.storage.saveIP(ip);
    }

    private void setDeviceInfo(String ip,String port){
        this.setDeviceInfo(ip,Integer.parseInt(port));
    }


    private boolean isServiceRunning(String serviceName) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(100)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void initCheckSwitchStatus(){
        Data data=new Data.Builder()
                .putInt("code",1)
                .build();
        UUID idTask=this. addWork(data);
        this.observeSwitchCondifition(idTask);
    }

    private void observeSwitchCondifition(UUID idTask){

        LiveData x=this.wm.getWorkInfoByIdLiveData(idTask);
        x.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo o) {
                Data x=o.getOutputData();

                int res=x.getInt("hasil", 2);
                if(res==-1){
                    Toast.makeText(getApplicationContext(), "TIMEOUT", Toast.LENGTH_SHORT).show();
                }else if(res==0){
                    binding.switch3.setChecked(false);
                }else if(res==1){
                    binding.switch3.setChecked(true);
                }

            }
        });

    }

    private UUID addWork(Data data){
        OneTimeWorkRequest task =new OneTimeWorkRequest.Builder(OneTimeServices.class)
                .setInputData(data)
                .build();
        this.wm.enqueue(task);

        return task.getId();
    }


    @Override
    public void onClick(View view) {
        if (view==this.binding.switch3){
            boolean status=this.binding.switch3.isChecked();
            this.binding.switch3.setChecked(!status);
            Data data=new Data.Builder()
                    .putInt("code",2)
                    .putBoolean("status",!status)
                    .build();
            UUID idTask=this.addWork(data);
            this.observeSwitchCondifition(idTask);



        }else if(view==this.binding.service){
            if(isServiceRunning(MainServices.getServiceName())){
                stopService(new Intent( this,MainServices.class ));
                Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
            }else{
                startService(new Intent( this, MainServices.class ));
                Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT).show();
            }

            this.binding.service.setChecked(isServiceRunning(MainServices.getServiceName()));
        }else if(view ==this.binding.save){
            String ip=this.binding.ipAdress.getText().toString();
            String port=this.binding.port.getText().toString();
            this.setDeviceInfo(ip,port);

        }
    }




}