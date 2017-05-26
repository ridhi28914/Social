package com.application.social.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.application.social.views.R;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

public class tweetTwitter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_twitter);
        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                .getActiveSession();
////        Uri uri=new Uri();

        final Intent intent = new ComposerActivity.Builder(getApplicationContext())
                .session(session)
//                .text("Love where you work")
                .hashtags("#twitter")
                .createIntent();
        startActivity(intent);
    }
}
