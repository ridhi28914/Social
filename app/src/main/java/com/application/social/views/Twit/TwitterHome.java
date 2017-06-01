package com.application.social.views.Twit;

import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.application.social.utils.UploadManager;
import com.application.social.views.Integrated;
import com.application.social.views.R;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TwitterHome extends ListActivity {
    private TextView nameTv;
    UploadManager uploadManager = new UploadManager();
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    String fragmentNumberOld;
    Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_home);

        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        fragmentNumberOld= sharedPreference.getString("fragmentNumberOld", null);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(TwitterHome.this, button1);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String choice = (String) item.getTitle();

                        if (choice.equalsIgnoreCase("Facebook"))
                        {
                            editor.putString("fragmentNumberOld","101");
                            editor.commit();
                        }
                        else if (choice.equalsIgnoreCase("Twitter"))
                        {
//            // TODO: 6/1/2017 calltwitter activity
                            editor.putString("fragmentNumberOld","104");
                            editor.commit();
                        }
                        else if (choice.equalsIgnoreCase("Instagram"))
                        {
                            editor.putString("fragmentNumberOld","102");
                            editor.commit();
                        }
                        else if (choice.equalsIgnoreCase("Pinterest"))
                        {
                            editor.putString("fragmentNumberOld","103");
                            editor.commit();
                        }
                        Intent i = new Intent(TwitterHome.this, Integrated.class);
                        startActivity(i);
                        finish();
                        return true;
                    }
                });

                popup.show();
            }
        });

        String userName = null;
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        userName=sharedPreference.getString("twitterUsername",null);
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(userName)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();

        setListAdapter(adapter);

    }
}

