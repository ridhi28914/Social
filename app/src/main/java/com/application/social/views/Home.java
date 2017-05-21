package com.application.social.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.linkedin.platform.APIHelper;
//import com.linkedin.platform.LISessionManager;
//import com.linkedin.platform.errors.LIApiError;
//import com.linkedin.platform.errors.LIAuthError;
//import com.linkedin.platform.listeners.ApiListener;
//import com.linkedin.platform.listeners.ApiResponse;
//import com.linkedin.platform.listeners.AuthListener;
//import com.linkedin.platform.utils.Scope;
import com.application.social.utils.CommonLib;
import com.application.social.utils.Instagram.InstagramHelper;
import com.application.social.utils.Instagram.InstagramListener;
import com.application.social.views.Insta.Photo;
import com.application.social.views.Pint.PintHome;
import com.application.social.views.Twit.TwitterFeed;
import com.application.social.views.Twit.TwitterHome;
import com.pinterest.android.pdk.PDKClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import io.fabric.sdk.android.Fabric;


import com.pinterest.android.pdk.PDKCallback;
//import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.List;

import static com.application.social.utils.CommonLib.PINTEREST_KEY;
import static com.application.social.utils.CommonLib.TWITTER_KEY;
import static com.application.social.utils.CommonLib.TWITTER_SECRET;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Home extends AppCompatActivity implements InstagramListener, View.OnClickListener {

    String TAG = "Home class";

    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
//    private static final String host = "api.linkedin.com";
//    private static final String liUrl = "https://" + host
//            + "/v1/people/~:" +
//            "(email-address,formatted-name,phone-numbers,picture-urls::(original))";

//    private ProgressDialog progress;
//    private TextView user_name, user_email;
//    private ImageView profile_picture;

    //    twitter login
    TwitterLoginButton twitterLoginButton;

    private PDKClient pdkClient;
    Button pinterestLoginButton;

    private Button mInstagramButton;
    private InstagramHelper mInstagram;
    private TextView mDataTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_home);

        pinterestLoginButton = (Button) findViewById(R.id.pinterest_login);
        pinterestLoginButton.setOnClickListener(this);
        pdkClient = PDKClient.configureInstance(this, PINTEREST_KEY);
        pdkClient.onConnect(this);

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        onTwitterLogin(twitterLoginButton);

        mInstagramButton = (Button) findViewById(R.id.instagram_button);
        mInstagramButton.setOnClickListener(this);
        mInstagram = new InstagramHelper(this, this, CommonLib.INSTAGRAM_ID,CommonLib.INSTAGRAM_SECRET, CommonLib.INSTAGRAM_CALLBACK_URL);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        PDKClient.getInstance().onOauthResponse(requestCode, resultCode, data);

        twitterLoginButton.onActivityResult(requestCode, resultCode, data);

//        LISessionManager.getInstance(getApplicationContext())
//                .onActivityResult(this,
//                        requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.pinterest_login:
                onPinterestLogin();
                break;
            case R.id.instagram_button:
                mInstagram.performSignIn();
                break;
        }
    }
    @Override public void onInstagramSignInFail(String errorMessage) {
        mDataTextView.setText(errorMessage);
    }

    @Override public void onInstagramSignInSuccess(String authToken, String userId) {
        System.out.print(authToken);
        show_photo_view(authToken);
    }
    private void show_photo_view(String authToken){
        Intent intent = new Intent(Home.this, Photo.class);
        Bundle bundle= new Bundle();
        bundle.putString("authToken",authToken);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void onPinterestLogin() {
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);

        pdkClient.login(this, scopes, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(getClass().getName(), response.getData().toString());
                onPinterestLoginSuccess();
                //                //user logged in, use response.getUser() to get PDKUser object
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });
    }

    private void onPinterestLoginSuccess() {
        Intent i = new Intent(this, PintHome.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
    public void onTwitterLogin(TwitterLoginButton twitterLoginButton) {

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("Twitter success "+ getClass().getName(), result.toString());
                onTwitterLoginSuccess(result.data);


            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }
    private void onTwitterLoginSuccess(TwitterSession result) {
        
//        // TODO: 4/27/2017 change this to fragment starting 
        //new Intent(this, fragments_view.class);
//        Intent intent = new Intent(getApplicationContext(), fragments_view.class);
//        startActivity(intent);
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        String userId=sharedPreference.getString("user_id",null);
        
        Bundle extras = new Bundle();
        extras.putString("fbGoId", String.valueOf(result.getUserId()));
        extras.putString("userName", result.getUserName());
        extras.putString("token", String.valueOf(result.getAuthToken()));
        Intent intent = new Intent(this, TwitterHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }



}