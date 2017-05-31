package com.application.social.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.hardware.camera2.params.Face;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.application.social.views.Fb.Facebook;
import com.application.social.views.R;

public class Integrated extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrated);
        FragmentManager transaction = getFragmentManager();
//        transaction.beginTransaction().add(R.id.container,Facebook,YOUR_FRAGMENT_STRING_TAG)//<---replace a view in your layout (id: container) with the newFragment
//                .commit();
    }
}
