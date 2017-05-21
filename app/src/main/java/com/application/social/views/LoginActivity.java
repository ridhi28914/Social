package com.application.social.views;

import android.support.v7.app.AppCompatActivity;
package info.androidhive.androidsplashscreentimer;
import android.support.v7.app.AppCompatActivity;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class LoginActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        String accessToken=sharedPreference.getString("access_token",null);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (accessToken!=null) {
                    Intent intent = new Intent(this, Home.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);


    }
}
