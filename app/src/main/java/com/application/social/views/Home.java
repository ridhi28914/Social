package com.application.social.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.application.social.data.UserDetails;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
//import com.twitter.sdk.android.core.identity.*;
//import com.twitter.sdk.android.core.Callback;
//import com.twitter.sdk.android.core.Result;
//import com.twitter.sdk.android.core.TwitterException;
//import com.twitter.sdk.android.core.TwitterSession;
//import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class Home extends AppCompatActivity {
    private TwitterLoginButton loginButton;


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "DqvYukCn5ZLGC1ypoPT3e9FrE";
    private static final String TWITTER_SECRET = "UiAX48Jc3ddksA8kln0CofaoLLSgtlde26DaJ7Mwat8qBMRT1h";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_home);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
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
                user.source=3;
//                user.profilePic=
                        String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


}
