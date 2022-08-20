package com.dimas519.chargingprotection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dimas519.chargingprotection.Presenter.ServicePresenter;
import com.dimas519.chargingprotection.databinding.FragmentServiceConfigurationBinding;

import java.util.List;


public class Fragment_Service_Configuration extends Fragment implements View.OnClickListener {
    private FragmentServiceConfigurationBinding binding;
    private ServicePresenter servicePresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.servicePresenter=new ServicePresenter(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding=FragmentServiceConfigurationBinding.inflate(inflater);

        long sleepCharging=this.servicePresenter.getSleepTimeCharging();
        long sleepOther=this.servicePresenter.getSleepTimeOther();

        //setup view
        if(sleepCharging!= -1){
            String minutes=(sleepCharging/60)+"";
            String second=(sleepCharging%60)+"";
            this.binding.minutesSleepCharging.setText(minutes);
            this.binding.secondSleepCharging.setText(second);
        }

        if(sleepOther!= -1){
            String minutes=(sleepCharging/60)+"";
            String second=(sleepCharging%60)+"";
            this.binding.minutesSleepOther.setText(minutes);
            this.binding.secondSleepOther.setText(second);
        }

        this.binding.slider.setValues(this.servicePresenter.getPercentage());
        this.binding.save.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if(view==this.binding.save){
            //save sleep time
            int min=Integer.parseInt(this.binding.minutesSleepCharging.getText().toString());
            int sec=Integer.parseInt(this.binding.secondSleepCharging.getText().toString());
            long sleepTime=((min* 60L)+sec);
            this.servicePresenter.setSleepTimeCharging(sleepTime);

            //save other time
            min=Integer.parseInt(this.binding.minutesSleepOther.getText().toString());
            sec=Integer.parseInt(this.binding.secondSleepOther.getText().toString());
            sleepTime=((min* 60L)+sec);
            this.servicePresenter.setSleepTimeOther(sleepTime);

            List<Float> sliderValues=this.binding.slider.getValues();
            this.servicePresenter.setPercentage(sliderValues);
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
    }
}