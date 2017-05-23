package com.application.social.views.Twit;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

import com.application.social.data.UserDetails;
import com.application.social.utils.UploadManager;
import com.application.social.views.R;

public class TwitterHome extends TabActivity {
    private TextView nameTv;
    UploadManager uploadManager = new UploadManager();
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_home);

        Bundle extras = getIntent().getExtras();
        Bundle extra=new Bundle();
        String userName = null;
        if(extras!=null) {
            userName = extras.getString("userName");
        }
        else{
            sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
            editor = sharedPreference.edit();
            userName=sharedPreference.getString("twitterUsername",null);
            if(userName!=null)
                extra.putString("userName",userName) ;

        }
        nameTv = (TextView) findViewById(R.id.name_textview);
        nameTv.setText(userName);

        TabHost tabHost = getTabHost();

        // Tab for user tweets
        TabHost.TabSpec photospec = tabHost.newTabSpec("MYTWEETS");
        // setting Title and Icon for the Tab
        photospec.setIndicator("MYTWEETS");
        Intent photosIntent = new Intent(this, TwitterFeed.class);
        photosIntent.putExtras(extra);

        photospec.setContent(photosIntent);

        tabHost.addTab(photospec); // Adding photos tab

    }

}

