package com.application.social.views;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class fragments_view extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments_view);

        Intent intent = getIntent();
        // Capture the layout's TextView and set the string as its text
        //TextView textView = (TextView) findViewById(R.id.twitter_check);
        //textView.setText("hi twitter successful!");
    }
}