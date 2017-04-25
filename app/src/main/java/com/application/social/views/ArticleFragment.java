package com.application.social.views;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ArticleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.activity_article_fragment, container, false);
//        try {
//            ConfigurationBuilder builder = new ConfigurationBuilder();
//
//            // GET THE CONSUMER KEY AND SECRET KEY FROM THE STRINGS XML
//            String TWITTER_CONSUMER_KEY = getActivity().getString(R.string.TWITTER_CONSUMER_KEY);
//            String TWITTER_CONSUMER_SECRET = getActivity().getString(R.string.TWITTER_CONSUMER_SECRET);
//
//            // TWITTER ACCESS TOKEN
//            String twit_access_token = twitPrefs.getString(PREF_KEY_OAUTH_TOKEN, null);
//
//            // TWITTER ACCESS TOKEN SECRET
//            String twit_access_token_secret = twitPrefs.getString(PREF_KEY_OAUTH_SECRET, null);
//
//            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
//            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//            builder.setOAuthAccessToken(twit_access_token);
//            builder.setOAuthAccessTokenSecret(twit_access_token_secret);
//            builder.setJSONStoreEnabled(true);
//            builder.setIncludeEntitiesEnabled(true);
//            builder.setIncludeMyRetweetEnabled(true);
//            builder.setIncludeRTsEnabled(true);
//
//            AccessToken accessToken = new AccessToken(twit_access_token, twit_access_token_secret);
//            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
//
//            Paging paging = new Paging(200); // MAX 200 IN ONE CALL. SET YOUR OWN NUMBER <= 200
//            statuses = twitter.getHomeTimeline(paging);
//
//            try {
//                String strInitialDataSet = DataObjectFactory.getRawJSON(statuses);
//                JSONArray JATweets = new JSONArray(strInitialDataSet);
//
//                for (int i = 0; i < JATweets.length(); i++) {
//                    JSONObject JOTweets = JATweets.getJSONObject(i);
//                    Log.e("TWEETS", JOTweets.toString());
//                }
    }
}
