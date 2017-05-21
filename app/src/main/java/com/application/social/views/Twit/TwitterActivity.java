package com.application.social.views.Twit;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.application.social.data.UserDetails;
import com.application.social.utils.ApiCall;
import com.application.social.utils.Twitter.MyTweet;
import com.application.social.utils.Twitter.MyTwitter;
import com.application.social.utils.Twitter.TwitterAuthenticated;
import com.application.social.utils.UploadManager;
import com.application.social.views.R;
import com.google.gson.Gson;

import com.loopj.android.http.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpRequestBase;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.application.social.utils.CommonLib.SERVER_URL;
import static com.application.social.utils.CommonLib.TWITTER_KEY;
import static com.application.social.utils.CommonLib.TWITTER_SECRET;

public class TwitterActivity extends ListActivity {
    final static String LOG_TAG = "rnc";
    private ListActivity activity;
    UploadManager uploadManager = new UploadManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        Bundle extras = getIntent().getExtras();
        UserDetails user= new UserDetails();

        user.setName(extras.getString("userName"));
//                user.email=session.getEmail();
        user.setToken(extras.getString("token"));
        user.setFbGoId(extras.getString("fbGoId"));
        user.setUserId(extras.getString("userId"));
//                user.profilePic=
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

        saveTwitterDb(user);

//        downloadTweets(user.getName());


    }

    public void downloadTweets(String ScreenName) {
//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadTwitterTask().execute(ScreenName);
//        } else {
//            Log.v(LOG_TAG, "No network connection available.");
//        }
    }

    private void saveTwitterDb(UserDetails user) {

        uploadManager.twitterLogIn(user);
    }


    // Uses an AsyncTask to download a Twitter user's timeline
    private class DownloadTwitterTask extends AsyncTask<String, Void, String> {
        final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
        final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
//        final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/home_timeline.json";

        @Override
        protected String doInBackground(String... screenNames) {
            String result = null;

            if (screenNames.length > 0) {
                result = getTwitterStream(screenNames[0],0);
            }
            return result;
        }

        // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
        @Override
        protected void onPostExecute(String result) {
            MyTwitter twits = jsonToTwitter(result);
//// TODO: 5/19/2017 twits contains json of tweets , show them  
            // lets write the results to the console as well
            for (MyTweet tweet : twits) {
                Log.i(LOG_TAG, tweet.getText());
            }

            // send the tweets to the adapter for rendering
//            ArrayAdapter<Tweet> adapter;
//            adapter = new ArrayAdapter<Tweet>(activity, layout.simple_list_item_1, twits);
//            setListAdapter(adapter);
        }


        // converts a string of JSON data into a Twitter object
        private MyTwitter jsonToTwitter(String result) {
            MyTwitter twits = null;
            if (result != null && result.length() > 0) {
                try {
                    Gson gson = new Gson();
                    twits = gson.fromJson(result, MyTwitter.class);
                } catch (IllegalStateException ex) {
                    // just eat the exception
                }
            }
            return twits;
        }

        // convert a JSON authentication object into an Authenticated object
        private TwitterAuthenticated jsonToAuthenticated(String rawAuthorization) {
            TwitterAuthenticated auth = null;
            if (rawAuthorization != null && rawAuthorization.length() > 0) {
                try {
                    Gson gson = new Gson();
                    auth = gson.fromJson(rawAuthorization, TwitterAuthenticated .class);
                } catch (IllegalStateException ex) {
                    // just eat the exception
                }
            }
            return auth;
        }

        private String getResponseBody(HttpRequestBase request) {
            StringBuilder sb = new StringBuilder();
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String reason = response.getStatusLine().getReasonPhrase();

                if (statusCode == 200) {

                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    sb.append(reason);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (ClientProtocolException ex1) {
            } catch (IOException ex2) {
            }
            return sb.toString();
        }

        private String getTwitterStream(String screenName,int i) {
            String results = null;

            // Step 1: Encode consumer key and secret
            try {
                // URL encode the consumer key and secret
                String urlApiKey = URLEncoder.encode(TWITTER_KEY, "UTF-8");
                String urlApiSecret = URLEncoder.encode(TWITTER_SECRET, "UTF-8");

                // Concatenate the encoded consumer key, a colon character, and the
                // encoded consumer secret
                String combined = urlApiKey + ":" + urlApiSecret;

                // Base64 encode the string
                String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                // Step 2: Obtain a bearer token
                HttpPost httpPost = new HttpPost(TwitterTokenURL);
                httpPost.setHeader("Authorization", "Basic " + base64Encoded);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
                String rawAuthorization = getResponseBody(httpPost);
                TwitterAuthenticated auth = jsonToAuthenticated(rawAuthorization);

                // Applications should verify that the value associated with the
                // token_type key of the returned object is bearer
                if (auth != null && auth.getToken_type().equals("bearer")) {

                    // Step 3: Authenticate API requests with bearer token
                    HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);
//                    HttpGet httpGet = new HttpGet(TwitterStreamURL);

                    // construct a normal HTTPS request and include an Authorization
                    // header with the value of Bearer <>
                    httpGet.setHeader("Authorization", "Bearer " + auth.getAccess_token());
                    httpGet.setHeader("Content-Type", "application/json");
                    // update the results with the body of the response
                    results = getResponseBody(httpGet);
                }
                else{
                    System.out.print("auth is null");
                }
            } catch (UnsupportedEncodingException ex) {

            }
            catch (IllegalStateException ex1) {

            }
            return results;
        }
    }
}
