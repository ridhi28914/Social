package com.application.social.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.social.data.UserDetails;
import com.application.social.utils.ApiCall;
import com.application.social.utils.CommonLib;
import com.application.social.utils.InstagramApp;
import com.application.social.utils.InstagramDialog;
import com.facebook.AccessToken;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
//import com.twitter.sdk.android.core.identity.*;
//import com.twitter.sdk.android.core.Callback;
//import com.twitter.sdk.android.core.Result;
//import com.twitter.sdk.android.core.TwitterException;
//import com.twitter.sdk.android.core.TwitterSession;
//import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.json.JSONObject;

import java.io.IOException;

import static android.graphics.Paint.Style.FILL;
import static com.application.social.utils.CommonLib.SERVER_URL;
import static com.application.social.utils.CommonLib.TWITTER_KEY;
import static com.application.social.utils.CommonLib.TWITTER_SECRET;
import static java.security.AccessController.getContext;

public class Home extends AppCompatActivity {

    String TAG = "Home class";

    //    twitter login
    private TwitterLoginButton twitterLoginButton;
    private static final String host = "api.linkedin.com";
    private static final String liUrl = "https://" + host
            + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,picture-urls::(original))";

    private ProgressDialog progress;
    private TextView user_name, user_email;
    private ImageView profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final InstagramApp mApp = new InstagramApp(this, CommonLib.CLIENT_ID,
//                CommonLib.INSTAGRAM_SECRET, CommonLib.INSTAGRAM_CALLBACK_URL);
//        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
//
//            @Override
//            public void onSuccess() {
//// tvSummary.setText("Connected as " + mApp.getUserName());
////                btnConnect.setText("Disconnect");
////                llAfterLoginView.setVisibility(View.VISIBLE);
//// userInfoHashmap = mApp.
//                mApp.fetchUserName();
//            }
//
//            @Override
//            public void onFail(String error) {
//                Toast.makeText(Home.this, error, Toast.LENGTH_SHORT).show();
//            }
//        });


        linkededinApiHelper();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_home);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                UserDetails user= new UserDetails();
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                user.name=session.getUserName();
//                user.email=session.getEmail();
                user.token= String.valueOf(session.getAuthToken());
                user.fbGoId= String.valueOf(session.getUserId());
//                user.profilePic=

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

//
//                String url = SERVER_URL+"twitter/login";
//                try {
//                    String response= ApiCall.POST(client,url,body);
//                    Log.d(TAG, "rspnse is:-" + response);
//                } catch (IOException e) {
//                    e.printStackTrace();
                    //                // TODO: 4/20/2017 return json exception response
//                Log.d(mTAG,"stack trace is :"+ e.printStackTrace());
               // }
                //new Intent(this, fragments_view.class);
                Intent intent = new Intent(getApplicationContext(), fragments_view.class);
                startActivity(intent);
                //ravleen commented it
//                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

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

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);
    }


    //LinkedIn login
    public void login(View view){
        final UserDetails userDetails=new UserDetails();
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
//// TODO: 4/22/2017 remove toast and code to store credentials in db
                        Toast.makeText(getApplicationContext(), "success" +
                                        LISessionManager
                                                .getInstance(getApplicationContext())
                                                .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();

                        LISessionManager s=LISessionManager.getInstance(getApplicationContext());
                        userDetails.setAccessToken(s.getSession().getAccessToken().toString());

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {

                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(Home.this, liUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    showResult(result.getResponseDataAsJson());
                    progress.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });
    }

    public  void  showResult(JSONObject response){

        try {
            String email=response.get("emailAddress").toString();
            String name=response.get("formattedName").toString();
            String pic=response.getString("pictureUrl");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}