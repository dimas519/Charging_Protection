package com.dimas519.chargingprotection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dimas519.chargingprotection.Presenter.WIFIPresenter;
import com.dimas519.chargingprotection.databinding.FragmentWifiConfigurationBinding;


public class Fragment_Wifi_Configuration extends Fragment implements View.OnClickListener {
    private FragmentWifiConfigurationBinding binding;
    private WIFIPresenter presenter;

    public Fragment_Wifi_Configuration(WIFIPresenter presenter){
        this.presenter=presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding=FragmentWifiConfigurationBinding.inflate(inflater);

        String ssid=this.presenter.getSSID();
        if(ssid!=null){
            this.binding.ssid.setText(ssid);
        }

        int timeout=this.presenter.getTimeout();
        this.binding.timeout.setText(timeout+"");


        //onclick listener
        this.binding.save.setOnClickListener(this);

        return this.binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if(view==this.binding.save){
            String ssid=this.binding.ssid.getText().toString();
            int timeout=Integer.parseInt(this.binding.timeout.getText().toString());
            if(ssid.equalsIgnoreCase("") ) {
                Toast.makeText(getContext(), "Fill the SSID", Toast.LENGTH_SHORT).show();
            }else if(timeout<1000){
                Toast.makeText(getContext(), "Timeout should more than 1000ms", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                this.presenter.saveSSID(ssid);
                this.presenter.saveTimeout(timeout);
            }


        }
    }
}