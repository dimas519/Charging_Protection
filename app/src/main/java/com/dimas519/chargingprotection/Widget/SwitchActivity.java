package com.dimas519.chargingprotection.Widget;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dimas519.chargingprotection.MainActivity;
import com.dimas519.chargingprotection.R;
import com.dimas519.chargingprotection.Storage.Storage;
import com.dimas519.chargingprotection.SwitchCharger;
import com.dimas519.chargingprotection.databinding.WidgetChargingProtectionBinding;


public class SwitchActivity extends BroadcastReceiver {



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        Storage x=new Storage(getApplicationContext());
//        SwitchCharger switchCharger=new SwitchCharger(x.getIP(),x.getPort());
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
//        StrictMode.setThreadPolicy(policy);
//
//        int currentState =  switchCharger.getstatus();
//
//        boolean res;
//        if(currentState==-1){
//            Toast.makeText(getApplicationContext(), "Switch Error", Toast.LENGTH_SHORT).show();
//        }else if(currentState==1){
//            res=switchCharger.turn_off();
//
//            if(res){
//                Toast.makeText(getApplicationContext(), "Switch Turned Off", Toast.LENGTH_SHORT).show();
//
//            }else{
//                Toast.makeText(getApplicationContext(), "Switch To Failed Turn Off", Toast.LENGTH_SHORT).show();
//            }
//
//
//        }else{
//            res=switchCharger.turn_on();
//            if(res){
//                Toast.makeText(getApplicationContext(), "Switch Turned On", Toast.LENGTH_SHORT).show();
//
//            }else{
//                Toast.makeText(getApplicationContext(), "Switch To Failed Turn On", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//
//
//
//    }


    @Override
    public void onReceive(Context context, Intent intent) {




        Intent x=new Intent(context,MainActivity.class);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        x.putExtra("hasil",1);


        Bundle bundle=new Bundle();
        bundle.putBoolean("res",false);

        context.startActivity(x,bundle);


    }
}