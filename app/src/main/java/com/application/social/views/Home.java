package com.application.social.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.linkedin.platform.APIHelper;
//import com.linkedin.platform.LISessionManager;
//import com.linkedin.platform.errors.LIApiError;
//import com.linkedin.platform.errors.LIAuthError;
//import com.linkedin.platform.listeners.ApiListener;
//import com.linkedin.platform.listeners.ApiResponse;
//import com.linkedin.platform.listeners.AuthListener;
//import com.linkedin.platform.utils.Scope;
import com.application.social.data.UserDetails;
import com.application.social.utils.CommonLib;
import com.application.social.utils.Instagram.InstagramHelper;
import com.application.social.utils.Instagram.InstagramListener;
import com.application.social.utils.UploadManager;
import com.application.social.views.Insta.Photo;
import com.application.social.views.Pint.PintHome;
import com.application.social.views.Twit.TwitterFeed;
import com.application.social.views.Twit.TwitterHome;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKUser;
import com.pinterest.android.pdk.Utils;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import io.fabric.sdk.android.Fabric;


import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.application.social.utils.CommonLib.PINTEREST_KEY;
import static com.application.social.utils.CommonLib.TWITTER_KEY;
import static com.application.social.utils.CommonLib.TWITTER_SECRET;
import static com.application.social.views.BuildConfig.DEBUG;
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
    private TextView nameTv;
    private ImageView profileIv;
    private final String USER_FIELDS = "id,image,counts,created_at,first_name,last_name,bio";
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
        txtstatus = (TextView) findViewById(R.id.txtStatus);
        login_button = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        login_button.setReadPermissions(Arrays.asList(
                "email", "user_birthday", "user_friends","user_posts"));
//        login_button.setPublishPermissions("publish_actions");
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
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
                                    uploadManager.facebookLogIn(userDetails);
                                } catch (JSONException e) {
                                    userDetails.setEmail("null");
                                    uploadManager.facebookLogIn(userDetails);
//                                    e.printStackTrace();
                                }

//                                // TODO: 4/24/2017 send request to get feed
                                Bundle params = new Bundle();
                                params.putString("fields", "picture,likes,comments,story,icon,message,place,shares");
                                params.putString("limit", "10");

                                new GraphRequest(
                                        AccessToken.getCurrentAccessToken(),
                                        "me/feed",
                                        params,
                                        HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse response) {
                                                Log.d(TAG, "response is "+ String.valueOf(response));
            /* handle the result */
                                            }
                                        }
                                ).executeAsync();
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
                onTwitterLoginSuccess(result.data);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }
    private void onTwitterLoginSuccess(TwitterSession result) {
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        String userId=sharedPreference.getString("user_id",null);
        editor.putString("twitterUsername", result.getUserName());
        editor.putString("twitter_login", "true");
        editor.commit();

        Bundle extras = new Bundle();
        extras.putString("userName",result.getUserName()) ;

        UserDetails user= new UserDetails();
        user.setName(result.getUserName());
//      user.email=session.getEmail();
        user.setToken(String.valueOf(result.getAuthToken()));
        user.setFbGoId(String.valueOf(result.getUserId()));
        user.setUserId(userId);
        saveTwitterDb(user);

        Intent intent = new Intent(this, TwitterHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    private void saveTwitterDb(UserDetails user) {

        uploadManager.twitterLogIn(user);
    }

}