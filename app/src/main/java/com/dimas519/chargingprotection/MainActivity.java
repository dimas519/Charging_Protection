package com.dimas519.chargingprotection;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import com.dimas519.chargingprotection.Presenter.WIFIPresenter;
import com.dimas519.chargingprotection.Tools.CODE;
import com.dimas519.chargingprotection.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private WIFIPresenter wifiPresenter;

    //0 fragment main
    private Fragment[] fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init-int
        this.binding =ActivityMainBinding.inflate(getLayoutInflater());
        this.fm=getSupportFragmentManager();

        //init presenter
        this.wifiPresenter =new WIFIPresenter(getApplicationContext());

        //init fragment
        this.fragments=new Fragment[CODE.NUMBER_OF_FRAGMENT];
        this.fragments[CODE.MainPage]=new MainFragment(this.wifiPresenter);

        //set fragments awal
        this.changePage(CODE.MainPage);

        //set up drawer dan onclick-nya
        this.binding.drawerButton.setOnClickListener(this);

        //drawer
        NavigationView navigationView=this.binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(CODE.MainPage).setChecked(true);

        this.checkAndPopUpPermission();


        setContentView(this.binding.getRoot());
    }

    private boolean checkPermissionForExternalStorage(String manifestPermission){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), manifestPermission);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    private void checkAndPopUpPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] allPermission=new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};

            String[] notGranted=new String[allPermission.length];
            int i=0;
            for(String permission : allPermission){
                if(!this.checkPermissionForExternalStorage(permission)){
                    notGranted[i]=permission;
                    i++;
                }
            }

            requestPermissions(notGranted,1);
        }
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
            this.binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int selected=item.getItemId();
        if(selected==R.id.nav_main) {
            changePage(CODE.MainPage);
        }else if(selected==R.id.nav_logging) {
            if (this.fragments[CODE.LoggingMenuPage] == null) {
                this.fragments[CODE.LoggingMenuPage] = new LoggingFragment();
            }
            changePage(CODE.LoggingMenuPage);
        }else if(selected==R.id.nav_about) {
            if(this.fragments[CODE.AboutPage]==null){
                this.fragments[CODE.AboutPage]=new Fragment_about();
            }
            changePage(CODE.AboutPage);
        }else if(selected==R.id.nav_wifi) {
            if(this.fragments[CODE.WifiConfigurationPage]==null){
                this.fragments[CODE.WifiConfigurationPage]=new Fragment_Wifi_Configuration(this.wifiPresenter);
            }
            changePage(CODE.WifiConfigurationPage);
        }else if(selected==R.id.nav_service) {
            if(this.fragments[CODE.ServicePage]==null){
                this.fragments[CODE.ServicePage]=new Fragment_Service_Configuration();
            }
            changePage(CODE.ServicePage);
        }

        this.binding.drawerLayout.closeDrawers();
        return true;
    }

}