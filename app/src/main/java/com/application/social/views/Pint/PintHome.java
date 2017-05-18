package com.application.social.views.Pint;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import com.application.social.views.R;

public class PintHome extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pint_home);

        TabHost tabHost = getTabHost();

        // Tab for Photos
        TabHost.TabSpec photospec = tabHost.newTabSpec("MYPINS");
        // setting Title and Icon for the Tab
        photospec.setIndicator("MYPINS");
        Intent photosIntent = new Intent(this, MyPins.class);
        photospec.setContent(photosIntent);

        // Tab for Songs
        TabHost.TabSpec songspec = tabHost.newTabSpec("MYBOARDS");
        songspec.setIndicator("MYBOARDS");
        Intent songsIntent = new Intent(this, MyBoards.class);
        songspec.setContent(songsIntent);

        // Tab for Videos
        TabHost.TabSpec videospec = tabHost.newTabSpec("FOLLOWING");
        videospec.setIndicator("FOLLOWING");
        Intent videosIntent = new Intent(this, FollowingActivity.class);
        videospec.setContent(videosIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        tabHost.addTab(videospec); // Adding videos tab
    }
}
