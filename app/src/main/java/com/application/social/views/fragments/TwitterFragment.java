package com.application.social.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.application.social.utils.UploadManager;
import com.application.social.views.R;
import com.application.social.views.Twit.TwitterFeed;
import com.application.social.views.fragments.BaseFragment;

/**
 * Created by Harsh on 31-05-2017.
 */

public class TwitterFragment extends BaseFragment {

    private TextView nameTv;
    UploadManager uploadManager = new UploadManager();
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    private View getView;
    private Context context;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_twitter_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView = getView();
        context = getContext();
        activity = getActivity();

        Bundle extras = activity.getIntent().getExtras();
        Bundle extra=new Bundle();
        String userName = null;
            sharedPreference = context.getSharedPreferences("TokenPreference", 0);
            editor = sharedPreference.edit();
            userName=sharedPreference.getString("twitterUsername",null);
            if(userName!=null)
                extra.putString("userName",userName) ;
        nameTv = (TextView) getView.findViewById(R.id.name_textview);
        nameTv.setText(userName);


        //Add code here
        TabHost tabHost = getTabHost();

        // Tab for user tweets
        TabHost.TabSpec photospec = tabHost.newTabSpec("MYTWEETS");
        // setting Title and Icon for the Tab
        photospec.setIndicator("MYTWEETS");
        Intent photosIntent = new Intent(context, TwitterFeed.class);
        photosIntent.putExtras(extra);

        photospec.setContent(photosIntent);

        tabHost.addTab(photospec); // Adding photos tab
    }
}
