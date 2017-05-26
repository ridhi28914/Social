package com.application.social.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.application.social.utils.UploadManager;
import com.application.social.views.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Configuration;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.net.URI;


import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;

import static com.application.social.utils.CommonLib.TWITTER_ACCESSTOKEN_KEY;
import static com.application.social.utils.CommonLib.TWITTER_ACCESSTOKEN_SECRET;
import static com.application.social.utils.CommonLib.TWITTER_KEY;
import static com.application.social.views.R.menu.photo;

public class Compose extends AppCompatActivity  {
//    SharedPreferences sharedPreference;
//    SharedPreferences.Editor editor;
    Context context = getApplicationContext();
UploadManager uploadManager =new UploadManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//            uploadManager.twitterPostTweet("message");

//        Uri imageUri = FileProvider.getUriForFile(Compose.this,
//                BuildConfig.APPLICATION_ID + ".file_provider",
//                new File("/path/to/image"));
//            uploadManager.twitterPostTweet("message");

//        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
//                .getActiveSession();
//////        Uri uri=new Uri();
//
//        final Intent intent = new ComposerActivity.Builder(this)
//                .session(session)
////                .text("Love where you work")
//                .hashtags("#twitter")
//                .createIntent();
//        startActivity(intent);

//        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
//        editor = sharedPreference.edit();
//        aceessToken = sharedPreference.getString("fbToken", null);
//        at = gson.fromJson(aceessToken, AccessToken.class);
//        onFacebookLoginSuccess2(accessToken);

        Log.i("Twitter activity", "Before building configuration");
//        TweetComposer.Builder builder = new TweetComposer.Builder(Compose.this)
//                .text("just setting up my Twitter Kit.");
////                    .image(imageUri);
//        builder.show();


    }
//    public void onFacebookLoginSuccess2(AccessToken accessToken) {
//
//        Bundle params = new Bundle();
//        params.putString("message", "This is a test message");
///* make the API call */
//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/me/feed",
//                params,
//                HttpMethod.POST,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        Log.d(TAG, "response is "+ String.valueOf(response));
//            /* handle the result */
//                    }
//                }
//        ).executeAsync();
//    }
}
