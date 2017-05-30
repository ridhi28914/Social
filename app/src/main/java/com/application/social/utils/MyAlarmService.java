package com.application.social.utils;

/**
 * Created by Ridhi on 5/30/2017.
 */

import android.app.Service;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.IBinder;

import android.widget.Toast;

import com.application.social.views.Compose;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

public class MyAlarmService extends Service {

    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    Compose compose=new Compose();
    @Override

    public void onCreate() {
        Gson gson = new Gson();
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        String message = sharedPreference.getString("schedule_message", null);
        String send_fb = sharedPreference.getString("send_fb", null);
        String send_twitter = sharedPreference.getString("send_twitter", null);

        if(send_fb=="true") {
            String accessToken = sharedPreference.getString("fbToken", null);
            AccessToken at = gson.fromJson(accessToken, AccessToken.class);
            compose.onFacebookLoginSuccess(at,message);
        }
        if(send_twitter=="true"){
            String twitterSession = sharedPreference.getString("twitterSession", null);
            TwitterSession ts = gson.fromJson(twitterSession, TwitterSession.class);
            compose.sendTweets(message,ts);
        }

//        Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();

    }



    @Override

    public IBinder onBind(Intent intent) {

// TODO Auto-generated method stub

        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();

        return null;

    }



    @Override

    public void onDestroy() {

// TODO Auto-generated method stub

        super.onDestroy();

        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();

    }



    @Override

    public void onStart(Intent intent, int startId) {

// TODO Auto-generated method stub

        super.onStart(intent, startId);

        Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();

    }



    @Override

    public boolean onUnbind(Intent intent) {

// TODO Auto-generated method stub

        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();

        return super.onUnbind(intent);

    }


}
