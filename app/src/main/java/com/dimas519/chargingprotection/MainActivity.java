package com.dimas519.chargingprotection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.dimas519.chargingprotection.Presenter.WifiPresenter;
import com.dimas519.chargingprotection.Tools.CODE;
import com.dimas519.chargingprotection.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private NavigationView navigationView;

    private WifiPresenter wifiPresenter;


    //0 fragment main
    private Fragment[] fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init-int
        this.binding =ActivityMainBinding.inflate(getLayoutInflater());
        this.fm=getSupportFragmentManager();

        this.wifiPresenter =new WifiPresenter(getApplicationContext());


        //init fragment
        this.fragments=new Fragment[CODE.NUMBER_OF_FRAGMENT];
        this.fragments[CODE.MainPage]=new MainFragment(wifiPresenter);

        //set fragments awal
        this.changePage(CODE.MainPage);

        //set up drawer dan onclick-nya
        this.binding.drawerButton.setOnClickListener(this);

        //drawer
        this.navigationView=this.binding.navView;
        this.navigationView.setNavigationItemSelectedListener(this);

        this.navigationView.getMenu().getItem(CODE.MainPage).setChecked(true);



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
                changePage(CODE.MainPage);
                break;
            case R.id.nav_logging:
                if(this.fragments[CODE.LoggingMenu]==null){
                    this.fragments[CODE.LoggingMenu]=new LoggingFragment();
                }
                changePage(CODE.LoggingMenu);
                break;
            case R.id.nav_about:
                if(this.fragments[CODE.About]==null){
                    this.fragments[CODE.About]=new Fragment_about();
                }
                changePage(CODE.About);
                break;
            case R.id.nav_wifi:
                if(this.fragments[CODE.WifiConfiguration]==null){
                    this.fragments[CODE.WifiConfiguration]=new Fragment_Wifi_Configuration(this.wifiPresenter);
                }
                changePage(CODE.WifiConfiguration);
                break;
        }


        this.binding.drawerLayout.closeDrawers();
        return true;
    }
}