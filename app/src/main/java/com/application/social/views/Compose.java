package com.application.social.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.application.social.utils.MultiSelectionSpinner;
import com.application.social.utils.UploadManager;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import retrofit2.Call;

public class Compose extends AppCompatActivity  implements MultiSelectionSpinner.OnMultipleItemsSelectedListener{
    private static final String TAG = "Compose.class";
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    AccessToken accessToken=null;
    MultiSelectionSpinner multiSelectionSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_16px);
        String[] array = {"Facebook", "Twitter"};
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(array);
        multiSelectionSpinner.setListener(this);


        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();

    }
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


//        Uri imageUri = FileProvider.getUriForFile(Compose.this,
//                BuildConfig.APPLICATION_ID + ".file_provider",
//                new File("/path/to/image"));


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_send:
                List<Integer> l = multiSelectionSpinner.getSelectedIndices();
                Log.d(TAG,"l is "+l.toString());
                if (l.contains(0) || l.contains(1)) {
                    if (l.contains(0)) {
                        String accessToken = sharedPreference.getString("fbToken", null);
                        Gson gson = new Gson();
                        AccessToken at = gson.fromJson(accessToken, AccessToken.class);
                        onFacebookLoginSuccess(at);
                    }
                    if (l.contains(1)) {
                        String twitterSession = sharedPreference.getString("twitterSession", null);
                        Gson gson = new Gson();
                        TwitterSession ts = gson.fromJson(twitterSession, TwitterSession.class);
                        sendTweets("trying testing", ts);
                    }
                }
                Toast.makeText(this, "Message sent.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compose, menu);
        return true;
    }
    private void sendTweets(String id,TwitterSession session) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<Tweet> update = statusesService.update(id, null, null, null, null, null, null, null, null);
        Log.d("TwitterKit", "Login with Twitter failure");
        update.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Log.e(TAG, "Result<Tweet> result" + result.data.toString());

            }

            @Override
            public void failure(com.twitter.sdk.android.core.TwitterException exception) {
                Log.e(TAG, "Result<Tweet> result" + exception.getMessage());
            }
        });
    }
    public void onFacebookLoginSuccess(AccessToken accessToken) {

        Bundle params = new Bundle();
        params.putString("message", "This is a test message again again");
/* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "response is "+ String.valueOf(response));
            /* handle the result */
                    }
                }
        ).executeAsync();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0 , R.anim.slide_out_right);

//        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_right);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

}
