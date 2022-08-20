package com.dimas519.chargingprotection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimas519.chargingprotection.databinding.FragmentAboutBinding;


public class Fragment_about extends Fragment implements View.OnClickListener {
    private FragmentAboutBinding binding;

    private final String github_Link="https://github.com/dimas519";
    private final String linkedin_Link="https://www.linkedin.com/in/dimas519/";
    private final String gitlab_link="https://gitlab.com/dimas519";


    private final String instagram_Link="https://www.instagram.com/dimas519/";
    private final String facebook_link="https://www.facebook.com/dimas519";



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding=FragmentAboutBinding.inflate(inflater);

        this.binding.tvAppName.setText(BuildConfig.VERSION_NAME);
        String versionCode=BuildConfig.VERSION_CODE+"";
        this.binding.tvAppCode.setText(versionCode);


        //onclick listener
        this.binding.githubBtn.setOnClickListener(this);
        this.binding.gitlabBtn.setOnClickListener(this);
        this.binding.linkedinBtn.setOnClickListener(this);
        this.binding.instagramBtn.setOnClickListener(this);
        this.binding.facebookBtn.setOnClickListener(this);


        return this.binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if(view==this.binding.githubBtn){
            openUrl(this.github_Link);
        }else if(view==this.binding.gitlabBtn){
            openUrl(this.gitlab_link);
        }else if(view==this.binding.linkedinBtn){
            openUrl(this.linkedin_Link);
        }else if(view==this.binding.instagramBtn){
            openUrl(this.instagram_Link);
        }else if(view==this.binding.facebookBtn){
            openUrl(this.facebook_link);
        }
    }

    private void openUrl(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }



}