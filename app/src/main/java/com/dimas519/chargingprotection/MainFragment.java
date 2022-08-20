package com.dimas519.chargingprotection;

import static android.content.Context.ACTIVITY_SERVICE;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.dimas519.chargingprotection.Presenter.SwitchPresenter;
import com.dimas519.chargingprotection.Presenter.WifiPresenter;
import com.dimas519.chargingprotection.Service.MainServices;
import com.dimas519.chargingprotection.Service.OneTimeServices;
import com.dimas519.chargingprotection.Tools.BatteryStatus;
import com.dimas519.chargingprotection.Tools.CODE;
import com.dimas519.chargingprotection.Tools.WifiChecker;
import com.dimas519.chargingprotection.Widget.Widget_Charging_Protection;
import com.dimas519.chargingprotection.databinding.FragmentMainBinding;
import java.util.UUID;


public class MainFragment extends Fragment implements View.OnClickListener {
    private FragmentMainBinding binding;
    private WorkManager wm;


    private WifiPresenter networkPresenter;
    private SwitchPresenter switchPresenter;

    public MainFragment(WifiPresenter presenter) {
        this.networkPresenter =presenter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.wm= WorkManager.getInstance(getContext());
        this.switchPresenter =new SwitchPresenter(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //init binding
        this.binding=FragmentMainBinding.inflate(inflater);


        //set up status
        IntentFilter ifilter= new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent battery = getContext().registerReceiver(null, ifilter);
        int status = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        this.binding.state.setText(BatteryStatus.getStatus(status));


        //setup text view
        String switchIP=this.switchPresenter.getIP();
        if(switchIP!=null) {
            this.binding.ipAdress.setText(switchIP);
        }
        this.binding.port.setText(this.switchPresenter.getPort()+"");

        //set up toogle
        initCheckSwitchStatus();
        this.binding.service.setChecked(isServiceRunning(MainServices.getServiceName()));


        //onclick Listener
        this.binding.switchPlug.setOnClickListener(this);
        this.binding.service.setOnClickListener(this);
        this.binding.save.setOnClickListener(this);




        return this.binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (view==this.binding.switchPlug){
            boolean status=this.binding.switchPlug.isChecked();
            this.binding.switchPlug.setChecked(!status);

            if(this.checkNetwork()) {
                Data data = new Data.Builder()
                        .putInt("code", 2)
                        .putBoolean("status", !status)
                        .build();
                UUID idTask = this.addWork(data);
                this.observeSwitchCondifition(idTask, true);
            }

        }else if(view==this.binding.service){
            if(isServiceRunning(MainServices.getServiceName())){
                getContext().stopService(new Intent( getContext(),MainServices.class ));
                Toast.makeText(getContext(), "Service Stopped", Toast.LENGTH_LONG).show();
            }else{
                getContext().startService(new Intent( getContext(), MainServices.class ));
                Toast.makeText(getContext(), "Service Started", Toast.LENGTH_SHORT).show();
            }

            this.binding.service.setChecked(isServiceRunning(MainServices.getServiceName()));
        }else if(view ==this.binding.save){
            String ip=this.binding.ipAdress.getText().toString();
            String port=this.binding.port.getText().toString();
            this.setDeviceInfo(ip,port);
            this.initCheckSwitchStatus();
        }
    }

    private  boolean isServiceRunning(String serviceName) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(100)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkNetwork(){
        String switchIP=this.switchPresenter.getIP();

        if(switchIP!=null){
            String SSID=this.networkPresenter.getSSID();
            if(!WifiChecker.wifiStatus(SSID,switchIP,getContext())){
                return true;
            }else{
                Toast.makeText(getContext(), "WIFI Saved MissMatch", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(getContext(), "Fill Smart Switch IP first", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initCheckSwitchStatus(){
        if(checkNetwork()) {
            Data data = new Data.Builder()
                    .putInt("code", 1)
                    .build();
            UUID idTask = this.addWork(data);
            this.observeSwitchCondifition(idTask);
        }
    }

    private void observeSwitchCondifition(UUID idTask){
        this.observeSwitchCondifition(idTask,false);
    }

    private void observeSwitchCondifition(UUID idTask, boolean updateWidget){

        LiveData x=this.wm.getWorkInfoByIdLiveData(idTask);
        x.observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo o) {
                Data x=o.getOutputData();

                int res=x.getInt("hasil", 2);
                if(res== CODE.ERROR){
                    Toast.makeText(getContext(), "TIMEOUT", Toast.LENGTH_SHORT).show();
                    binding.switchPlug.setChecked(false);
                }else if(res==CODE.OFF){
                    binding.switchPlug.setChecked(false);

                }else if(res==CODE.ON){
                    binding.switchPlug.setChecked(true);
                }

                if(updateWidget) {
                    Intent intent = new Intent(getContext(), Widget_Charging_Protection.class);
                    intent.setAction(CODE.ChangeStatus);
                    intent.putExtra("status", res);
                    getContext().sendBroadcast(intent);
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

    private void setDeviceInfo(String ip,int port){
        this.binding.ipAdress.setText(ip);
        this.binding.port.setText(port+"");

        this.switchPresenter.savePort(port);
        this.switchPresenter.saveIP(ip);
    }

    private void setDeviceInfo(String ip,String port){
        this.setDeviceInfo(ip,Integer.parseInt(port));
    }

    @Override
    public void onResume() {
        super.onResume();
        this.initCheckSwitchStatus();
    }

}