package com.dimas519.chargingprotection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dimas519.chargingprotection.databinding.ActivityMainBinding;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;



    //0 fragment main
    private ArrayList<Fragment> fragments;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int temp=data.getIntExtra("hasil",9);
        Toast.makeText(getApplicationContext(), "Switch Turned On"+temp, Toast.LENGTH_SHORT).show();
        Log.d("koncisss", "onActivityResult: "+temp);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);










        //init-int
        this.binding =ActivityMainBinding.inflate(getLayoutInflater());
        this.fm=getSupportFragmentManager();



        //init fragment
        this.fragments=new ArrayList<>();
        this.fragments.add(new MainFragment(0));


        //set fragments awal
        this.changePage(0);

//        ft=fm.beginTransaction();
//        fm.setFragmentResultListener("sebelum",this,this);





        setContentView(binding.getRoot());
    }



    public void changePage(int page){
        this.ft=fm.beginTransaction();
        if(page==0){
            this.ft.add(this.binding.fragments.getId(),fragments.get(0),null);
        }
        ft.commit();
    }















}