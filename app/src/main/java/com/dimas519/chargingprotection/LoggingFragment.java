package com.dimas519.chargingprotection;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dimas519.chargingprotection.Presenter.LoggingPresenter;
import com.dimas519.chargingprotection.Service.MainServices;
import com.dimas519.chargingprotection.Tools.ServiceHelper;
import com.dimas519.chargingprotection.databinding.FragmentLoggingBinding;

public class LoggingFragment extends Fragment implements View.OnClickListener {
    private FragmentLoggingBinding binding;
    private LoggingPresenter presenter;


    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding=FragmentLoggingBinding.inflate(inflater);
        this.presenter=new LoggingPresenter(getContext()); //init disini karena presenter tidak dibutuhkan ditempat lain;

        //set view
        this.binding.ipAdress.setText(this.presenter.getIP());
        this.binding.port.setText(this.presenter.getPort()+"");
        this.binding.switch3.setChecked(this.presenter.getStatus());



        //onclick listener
        this.binding.save.setOnClickListener(this);

        return this.binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if(view==this.binding.save){
            String ip=this.binding.ipAdress.getText().toString();
            int port=Integer.parseInt(this.binding.port.getText().toString());
            boolean status=this.binding.switch3.isChecked();

            this.presenter.saveIP(ip);
            this.presenter.savePort(port);
            this.presenter.saveStatus(status);

            Toast.makeText(getContext(), "Logging Saved", Toast.LENGTH_SHORT).show();
            ServiceHelper.restartService(requireContext(), MainServices.getServiceName(),MainServices.class);
        }
    }
}