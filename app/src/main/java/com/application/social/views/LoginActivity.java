package com.application.social.views;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.content.SharedPreferences;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

import static com.application.social.utils.CommonLib.TWITTER_KEY;
import static com.application.social.utils.CommonLib.TWITTER_SECRET;


public class LoginActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
                editor = sharedPreference.edit();
                String accessToken = sharedPreference.getString("access_token", null);
                editor.putString("fragmentNumberOld", "0");
                editor.commit();
                // This method will be executed once the timer is over
                // Start your app main activity
                if (accessToken != null) {

                    sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
                    editor = sharedPreference.edit();
//                    if (sharedPreference.getString("facebook_login", null) != null) {
//                        Intent i = new Intent(LoginActivity.this, FacebookFeed.class);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
//                        finish();
//                    }
//                    else{
                        Intent intent = new Intent(LoginActivity.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
//                }
                else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    }

