package com.dimas519.chargingprotection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dimas519.chargingprotection.Presenter.NetworkPresenter;
import com.dimas519.chargingprotection.Tools.PageCode;
import com.dimas519.chargingprotection.databinding.ActivityMainBinding;
import com.dimas519.chargingprotection.databinding.FragmentLoggingBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private NavigationView navigationView;
    private NetworkPresenter networkPresenter;




    //0 fragment main
    private Fragment[] fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init-int
        this.binding =ActivityMainBinding.inflate(getLayoutInflater());
        this.fm=getSupportFragmentManager();
        this.networkPresenter=new NetworkPresenter(getApplicationContext());


        //init fragment
        this.fragments=new Fragment[PageCode.NUMBER_OF_FRAGMENT];
        this.fragments[PageCode.MainPage]=new MainFragment(0);

        //set fragments awal
        this.changePage(PageCode.MainPage);

        //set up drawer dan onclick-nya
        this.binding.drawerButton.setOnClickListener(this);

        //drawer
        this.navigationView=this.binding.navView;
        this.navigationView.setNavigationItemSelectedListener(this);

        this.navigationView.getMenu().getItem(PageCode.MainPage).setChecked(true);



//        ft=fm.beginTransaction();
//        fm.setFragmentResultListener("sebelum",this,this);

        setContentView(this.binding.getRoot());
    }



    private void changePage(int page){
        this.ft=this.fm.beginTransaction();
        this.hideOtherFragments(page);
        if(this.fragments[page].isAdded()) {
            this.ft.show(this.fragments[page]);
        }else {
            this.ft.add(this.binding.fragment.getId(), this.fragments[page], null);
        }
        this.ft.commit();
    }

    private void hideOtherFragments(int pageToShow){
        for (int i=0;i<this.fragments.length;i++){
            if(i!=pageToShow && this.fragments[i]!=null){
                this.ft.hide(fragments[i]);
            }
        }
    }




    @Override
    public void onClick(View view) {
        if(view==this.binding.drawerButton){
            this.binding.drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_main:
                changePage(PageCode.MainPage);
                break;
            case R.id.nav_logging:
                if(this.fragments[PageCode.LoggingMenu]==null){
                    this.fragments[PageCode.LoggingMenu]=new LoggingFragment();
                }
                changePage(PageCode.LoggingMenu);
                break;
            case R.id.nav_about:
                if(this.fragments[PageCode.About]==null){
                    this.fragments[PageCode.About]=new Fragment_about();
                }
                changePage(PageCode.About);
                break;
            case R.id.nav_wifi:
                if(this.fragments[PageCode.WifiConfiguration]==null){
                    this.fragments[PageCode.WifiConfiguration]=new Fragment_Wifi_Configuration(this.networkPresenter);
                }
                changePage(PageCode.WifiConfiguration);
                break;
        }


        this.binding.drawerLayout.closeDrawers();
        return true;
    }
}