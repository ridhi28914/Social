package com.application.social.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.application.social.data.UserDetails;
import com.application.social.utils.ApiCall;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.application.social.utils.CommonLib.SERVER_URL;

public class Twitter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        Bundle extras = getIntent().getExtras();
        UserDetails user= new UserDetails();

        user.setName(extras.getString("userName"));
//                user.email=session.getEmail();
        user.setToken(extras.getString("token"));
        user.setFbGoId(extras.getString("fbGoId"));
//                user.profilePic=
//// TODO: 4/22/2017 Get the email address of user
//                TwitterAuthClient authClient = new TwitterAuthClient();
//                authClient.requestEmail(session, new Callback<String>() {
//                    @Override
//                    public void success(Result<String> result) {
//                        // Do something with the result, which provides the email address
//                        String session2=result.data;
//                        Toast.makeText(getApplicationContext(), session2, Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//                        // Do something on failure
//                        Toast.makeText(getApplicationContext(), "No email ", Toast.LENGTH_LONG).show();
//                    }
//                });

        saveTwitterDb(user);



    }

    private void saveTwitterDb(UserDetails user) {

                OkHttpClient client;
                client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
//                        .add("email", cred.email)
                        .add("name", user.name)
                        .add("client_id", "social_android_client")
                        .add("app_type", "social_android")
                        .add("fbGoId", user.fbGoId)
//                        .add("profile_pic", cred.profilePic)
                        .add("token", user.token)
                        .build();


                String url = SERVER_URL+"twitter/login";
                try {
                    String response= ApiCall.POST(client,url,body);
//                    Log.d(TAG, "rspnse is:-" + response);
                } catch (IOException e) {
                    e.printStackTrace();
                        // TODO: 4/20/2017 return json exception response
//                Log.d(mTAG,"stack trace is :"+ e.printStackTrace());
         }

    }
}
