package com.application.social.views.Twit;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.application.social.views.Pint.MyPins;
import com.application.social.views.Pint.PintHome;
import com.application.social.views.R;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;
import com.pinterest.android.pdk.Utils;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;

import static com.application.social.views.BuildConfig.DEBUG;
import static com.loopj.android.http.AsyncHttpClient.log;

public class TwitHome extends TabActivity {
    private TextView nameTv;
    private ImageView profileIv;
    PDKUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twit_home);
        log.d("inside twithome", "indise twit home");
        Bundle extras = getIntent().getExtras();
        String userName = null;
        if(extras!=null) {
            userName = extras.getString("userName");
        }
        nameTv = (TextView) findViewById(R.id.name_textview);
        nameTv.setText(userName);
        profileIv = (ImageView) findViewById(R.id.profile_imageview);
        TabHost tabHost = getTabHost();

        // Tab for user tweets
        TabHost.TabSpec photospec = tabHost.newTabSpec("MYTWEETS");
        // setting Title and Icon for the Tab
        photospec.setIndicator("MYTWEETS");
        Intent photosIntent = new Intent(this, TwitterTry.class);
        photospec.setContent(photosIntent);

        tabHost.addTab(photospec); // Adding photos tab
    }
}
