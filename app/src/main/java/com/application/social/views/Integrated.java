package com.application.social.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.hardware.camera2.params.Face;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.application.social.views.Fb.Facebook;
import com.application.social.views.R;
import com.application.social.views.fragments.FacebookFragment;

public class Integrated extends AppCompatActivity {

    FacebookFragment facebookFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrated);

        facebookFragment = new FacebookFragment();

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_container, facebookFragment);

        transaction.commit();
    }
}
