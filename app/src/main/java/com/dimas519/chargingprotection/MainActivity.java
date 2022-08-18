package com.dimas519.chargingprotection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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





    //0 fragment main
    private ArrayList<Fragment> fragments;


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
        this.changePage(PageCode.MainPage);

        //set up drawer dan onclick-nya
        this.binding.drawerButton.setOnClickListener(this);

        //drawer
        this.navigationView=this.binding.navView;
        this.navigationView.setNavigationItemSelectedListener(this);








//        ft=fm.beginTransaction();
//        fm.setFragmentResultListener("sebelum",this,this);

        setContentView(this.binding.getRoot());
    }



    private void changePage(int page){
        this.ft=this.fm.beginTransaction();
        this.hideOtherFragments(page);
        if(this.fragments.get(page).isAdded()) {
            this.ft.show(this.fragments.get(page));
        }else {
            this.ft.add(this.binding.fragment.getId(), this.fragments.get(page), null);
        }
        this.ft.commit();
    }

    private void hideOtherFragments(int pageToShow){
        for (int i=0;i<this.fragments.size();i++){
            if(i!=pageToShow){
                this.ft.hide(fragments.get(i));
            }
        }
    }

    private boolean initialized(int page){
        System.out.println(this.fragments.size()+" : "+page+","+ (this.fragments.size() > page) );
        if( this.fragments.size() > page &&this.fragments.get(page)!=null){
            return true;
        }else{
            return false;
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
                if(!this.initialized(PageCode.LoggingMenu)){
                    this.fragments.add(new LoggingFragment());
                }
                changePage(PageCode.LoggingMenu);

        }


        this.binding.drawerLayout.closeDrawers();
        return true;
    }
}