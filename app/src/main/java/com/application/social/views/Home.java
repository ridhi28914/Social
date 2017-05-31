package com.application.social.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.application.social.data.UserDetails;
import com.application.social.utils.AfterUpload;
import com.application.social.utils.CommonLib;
import com.application.social.utils.Instagram.InstagramHelper;
import com.application.social.utils.Instagram.InstagramListener;
import com.application.social.utils.UploadManager;
import com.application.social.views.Insta.Photoo;
import com.application.social.views.Pint.PintHome;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;


import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.application.social.utils.CommonLib.PINTEREST_KEY;

public class Home extends AppCompatActivity implements InstagramListener, View.OnClickListener,AfterUpload {

    String TAG = "Home class";
    TwitterSession session;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    //    twitter login
    TwitterLoginButton twitterLoginButton;

    private PDKClient pdkClient;
    ImageButton pinterestLoginButton;
    PDKUser user;

    private ImageButton mInstagramButton;
    private InstagramHelper mInstagram;
    private TextView mDataTextView;

    TextView txtstatus;
    LoginButton login_button;
    CallbackManager callbackManager;

    UploadManager uploadManager = new UploadManager();
    UserDetails userDetails = new UserDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UploadManager.addCallback(this);

//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        pinterestLoginButton = (ImageButton) findViewById(R.id.pinterest_login);
        pinterestLoginButton.setOnClickListener(this);
        pdkClient = PDKClient.configureInstance(this, PINTEREST_KEY);
        pdkClient.onConnect(this);

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        onTwitterLogin(twitterLoginButton);

        mInstagramButton = (ImageButton) findViewById(R.id.instagram_button);
        mInstagramButton.setOnClickListener(this);
        mInstagram = new InstagramHelper(this, this, CommonLib.INSTAGRAM_ID, CommonLib.INSTAGRAM_SECRET, CommonLib.INSTAGRAM_CALLBACK_URL);

        loginWithFB();

    }


    private void loginWithFB() {
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        final String userId = sharedPreference.getString("user_id", null);
        //fb login
//        txtstatus = (TextView) findViewById(R.id.txtStatus);
        login_button = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        login_button.setReadPermissions(Arrays.asList(
                "email", "user_birthday", "user_friends", "user_posts"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                final AccessToken accessToken = AccessToken.getCurrentAccessToken();
                LoginManager.getInstance().logInWithPublishPermissions(Home.this, Arrays.asList("publish_actions"));
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.v("LoginActivity ", response.toString());
                                try {
                                    userDetails.setName(object.getString("name"));
                                    userDetails.setUserId(object.getString("id"));
                                    userDetails.setProfilePic("https://graph.facebook.com/" + userDetails.getUserId() + "/picture?type=small");
                                    if (object.getString("email") != null)
                                        userDetails.setEmail(object.getString("email"));

                                    Log.d(TAG, "user is " + userDetails);
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
        String g = gson.toJson(accessToken);

        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        editor.putString("fbToken", g);
        editor.putString("facebook_login", "true");
        editor.putString("fragmentNumberNew","101");
        editor.commit();

//        Intent i = new Intent(this, FacebookFeed.class);
        Intent i = new Intent(this, Integrated.class);
//        Bundle extras = new Bundle();
//        extras.putString("token", g);
//        i.putExtras(extras);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }
    private void logOut() {

        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        String at= sharedPreference.getString("access_token", null);
        if(at!=null){
            uploadManager.logout("at");
            finish();
        }
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
            case R.id.fab:
                startActivity(new Intent(Home.this, Compose.class));
                overridePendingTransition(R.anim.slide_in_right, 0);
                break;

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onInstagramSignInFail(String errorMessage) {
        mDataTextView.setText(errorMessage);
    }

    @Override
    public void onInstagramSignInSuccess(String authToken, String userId) {
        System.out.print(authToken);
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        editor.putString("instagramToken", authToken);
        editor.putString("instagram_login", "true");
        editor.putString("fragmentNumberNew","102");
        editor.commit();
        UserDetails user = new UserDetails();
        String user_id = sharedPreference.getString("user_id", null);
        user.setUserId(user_id);
        user.setFbGoId(userId);
        user.setToken(authToken);
        //saveInstagramDb(user);

        show_photo_view(authToken);
    }

    private void show_photo_view(String authToken) {
        Intent intent = new Intent(Home.this, Photoo.class);
        Bundle bundle = new Bundle();
        bundle.putString("authToken", authToken);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void saveInstagramDb(UserDetails user) {
        uploadManager.instagramLogIn(user);
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
//                sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
//                editor = sharedPreference.edit();
//                editor.putString("pinterest_pdkclient", String.valueOf(pdkClient));
//                editor.commit();
                user = response.getUser();
                sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
                editor = sharedPreference.edit();
                editor.putString("fragmentNumberNew","103");
                editor.commit();
//                String userId=sharedPreference.getString("user_id",null);
//                UserDetails userDetails= new UserDetails();
//                userDetails.setUserId(userId);
//                userDetails.setName(user.getFirstName());
//                userDetails.setProfilePic(user.getImageUrl());
//                userDetails.setFbGoId(user.getUid());
//                savePinterestDb(userDetails);
                onPinterestLoginSuccess();
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
                Log.d("Twitter success " + getClass().getName(), result.toString());

                TwitterSession session = Twitter.getSessionManager()
                        .getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                onTwitterLoginSuccess(result.data, authToken, session);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    private void onTwitterLoginSuccess(TwitterSession result, TwitterAuthToken authToken, TwitterSession session) {

        Gson gson = new Gson();
        String g = gson.toJson(session);
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        String userId = sharedPreference.getString("user_id", null);
        editor.putString("twitterUsername", result.getUserName());
        editor.putString("twitter_login", "true");
        editor.putString("twitter_token", authToken.token);
        editor.putString("twitter_secret", authToken.secret);
        editor.putString("twitterSession", g);
        editor.putString("fragmentNumberNew","104");
        editor.commit();

        Bundle extras = new Bundle();
        extras.putString("userName", result.getUserName());

        UserDetails user = new UserDetails();
        user.setName(result.getUserName());
        user.setToken(String.valueOf(result.getAuthToken()));
        user.setFbGoId(String.valueOf(result.getUserId()));
        user.setUserId(userId);
//        saveTwitterDb(user);
        Intent intent = new Intent(this, Integrated.class);
//        Intent intent = new Intent(this, AllTabs2.class);
//        Intent intent = new Intent(this, Compose.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }


    private void saveTwitterDb(UserDetails user) {

        uploadManager.twitterLogIn(user);
    }



    @Override
    public void doneLoggingOut(String message) {
        if (message == "SUCCESS") {
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(Home.this, "Logout Failed." ,Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void doneTwitterLogIn() {

    }

    @Override
    public void donePinterestLogIn() {

    }

    @Override
    public void doneFacebookLogIn() {

    }

    @Override
    public void doneInstagramLogIn() {

    }

    @Override
    public void doneTwitterPost() {

    }
    @Override
    public void doneLoggingIn() {

    }

    @Override
    public void doneLoggingIn(String message) {

    }

}