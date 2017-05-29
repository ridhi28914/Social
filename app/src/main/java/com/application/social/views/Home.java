package com.application.social.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.social.data.UserDetails;
import com.application.social.utils.CommonLib;
import com.application.social.utils.Instagram.InstagramHelper;
import com.application.social.utils.Instagram.InstagramListener;
import com.application.social.utils.UploadManager;
import com.application.social.views.Fb.FacebookFeed;
import com.application.social.views.Insta.Photoo;
import com.application.social.views.Pint.PintHome;
import com.application.social.views.Twit.TwitterHome;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.gson.Gson;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;


import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

import static com.application.social.utils.CommonLib.PINTEREST_KEY;

public class Home extends AppCompatActivity implements InstagramListener, View.OnClickListener {

    String TAG = "Home class";
    TwitterSession session;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;

    //    twitter login
    TwitterLoginButton twitterLoginButton;

    private PDKClient pdkClient;
    Button pinterestLoginButton;
    PDKUser user;

    private Button mInstagramButton;
    private InstagramHelper mInstagram;
    private TextView mDataTextView;

    TextView txtstatus;
    LoginButton login_button;
    CallbackManager callbackManager;

    UploadManager uploadManager = new UploadManager();
    UserDetails userDetails =  new UserDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        pinterestLoginButton = (Button) findViewById(R.id.pinterest_login);
        pinterestLoginButton.setOnClickListener(this);
        pdkClient = PDKClient.configureInstance(this, PINTEREST_KEY);
        pdkClient.onConnect(this);

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        onTwitterLogin(twitterLoginButton);

        mInstagramButton = (Button) findViewById(R.id.instagram_button);
        mInstagramButton.setOnClickListener(this);
        mInstagram = new InstagramHelper(this, this, CommonLib.INSTAGRAM_ID,CommonLib.INSTAGRAM_SECRET, CommonLib.INSTAGRAM_CALLBACK_URL);



        loginWithFB();

    }

    private void loginWithFB() {
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        final String userId=sharedPreference.getString("user_id",null);
        //fb login
//        txtstatus = (TextView) findViewById(R.id.txtStatus);
        login_button = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        login_button.setReadPermissions(Arrays.asList(
                "email", "user_birthday", "user_friends","user_posts"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                final AccessToken accessToken=AccessToken.getCurrentAccessToken();
                LoginManager.getInstance().logInWithPublishPermissions(Home.this,Arrays.asList("publish_actions"));
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.v("LoginActivity ", response.toString());
                                try {
                                    userDetails.setName(object.getString("name"));
                                    userDetails.setUserId(object.getString("id"));
                                    userDetails.setProfilePic("https://graph.facebook.com/" + userDetails.getUserId()+ "/picture?type=small");
                                    if(object.getString("email") != null)
                                        userDetails.setEmail(object.getString("email"));

                                    Log.d(TAG,"user is "+userDetails);
//                                    uploadManager.facebookLogIn(userDetails);
                                } catch (JSONException e) {
                                    userDetails.setEmail("null");
//                                    uploadManager.facebookLogIn(userDetails);
//                                    e.printStackTrace();
                                }

                                onFacebookLoginSuccess(accessToken);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

                userDetails.setToken(loginResult.getAccessToken().getToken());
                userDetails.setFbGoId(loginResult.getAccessToken().getUserId());
                userDetails.setFacebookData(loginResult.getAccessToken().getApplicationId());
                userDetails.setSource(0);
                userDetails.setUserId(userId);
                String declinedPerm = String.valueOf(loginResult.getAccessToken().getDeclinedPermissions());
                System.out.print(declinedPerm);


//              Date expiresOn=loginResult.getAccessToken().getExpires();
            }

            @Override
            public void onCancel() {
                txtstatus.setText("login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                txtstatus.setText("login error: " + error.getMessage());
            }


        });

    }
    public void onFacebookLoginSuccess(AccessToken accessToken) {
        Gson gson = new Gson();
        String g= gson.toJson(accessToken);

        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        editor.putString("fbToken", g);
        editor.putString("facebook_login", "true");
        editor.commit();

        Intent i = new Intent(this, FacebookFeed.class);
        Bundle extras = new Bundle();
        extras.putString("token", g);
        i.putExtras(extras);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        callbackManager.onActivityResult(requestCode, resultCode, data);

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
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        editor.putString("instagramToken", authToken);
        editor.putString("instagram_login", "true");
        editor.commit();
        UserDetails user= new UserDetails();
        String user_id=sharedPreference.getString("user_id",null);
        user.setUserId(user_id);
        user.setFbGoId(userId);
        user.setToken(authToken);
        //saveInstagramDb(user);

        show_photo_view(authToken);
    }

    private void show_photo_view(String authToken){
        Intent intent = new Intent(Home.this, Photoo.class);
        Bundle bundle= new Bundle();
        bundle.putString("authToken",authToken);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    //private void saveInstagramDb(UserDetails user) {
    //    uploadManager.instagramLogIn(user);
    //}


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
//                sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
//                editor = sharedPreference.edit();
//                editor.putString("pinterest_pdkclient", String.valueOf(pdkClient));
//                editor.commit();
                user = response.getUser();
                sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
                editor = sharedPreference.edit();
//                String userId=sharedPreference.getString("user_id",null);
//                UserDetails userDetails= new UserDetails();
//                userDetails.setUserId(userId);
//                userDetails.setName(user.getFirstName());
//                userDetails.setProfilePic(user.getImageUrl());
//                userDetails.setFbGoId(user.getUid());
//                savePinterestDb(userDetails);
//                onPinterestLoginSuccess();
                //                //user logged in, use response.getUser() to get PDKUser object
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });
    }
    private void savePinterestDb(UserDetails user) {
        uploadManager.pinterestLogIn(user);
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

                TwitterSession session = Twitter.getSessionManager()
                        .getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                onTwitterLoginSuccess(result.data,authToken,session);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }
    private void onTwitterLoginSuccess(TwitterSession result, TwitterAuthToken authToken,TwitterSession session) {
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        String userId=sharedPreference.getString("user_id",null);
        editor.putString("twitterUsername", result.getUserName());
        editor.putString("twitter_login", "true");
        editor.putString("twitter_token", authToken.token);
        editor.putString("twitter_secret", authToken.secret);
        editor.commit();

        Bundle extras = new Bundle();
        extras.putString("userName",result.getUserName()) ;

        UserDetails user= new UserDetails();
        user.setName(result.getUserName());
        user.setToken(String.valueOf(result.getAuthToken()));
        user.setFbGoId(String.valueOf(result.getUserId()));
        user.setUserId(userId);
//       saveTwitterDb(user);
//        getTweets("dtrying",session);
        Intent intent = new Intent(this, TwitterHome.class);
//        Intent intent = new Intent(this, Compose.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    private void getTweets(String id,TwitterSession session) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<Tweet> update = statusesService.update(id, null, null, null, null, null, null, null,"some");
                        Log.d("TwitterKit", "Login with Twitter failure");
        update.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Log.e(TAG, "Result<Tweet> result" + result.data.toString());

            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, "Result<Tweet> result" + exception.getMessage());
            }
        });
//        statusesService.update(id, null, null, null, null, null, null, null,
//                new Callback<Tweet>() {
//            @Override
//            public void success(Result<Tweet> result) {
//                System.out.print("ridhi");
//            }
//            @Override
//            public void failure(TwitterException exception) {
//                Log.d("TwitterKit", "Login with Twitter failure", exception);
//
//            }
//        });
    }

    private void saveTwitterDb(UserDetails user) {

        uploadManager.twitterLogIn(user);
    }

}