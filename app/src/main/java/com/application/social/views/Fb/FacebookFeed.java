package com.application.social.views.Fb;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.application.social.utils.Facebook.FbAdapter;
import com.application.social.utils.Facebook.FbVersion;
import com.application.social.utils.Instagram.InstaImageAdapter;
import com.application.social.views.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FacebookFeed extends AppCompatActivity {
    String TAG = "Facebookfeed class";
    private RecyclerView recyclerView;
    ArrayList arrayLists = new ArrayList<>();
    private FbAdapter adapter;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_feed);

        Bundle extras = getIntent().getExtras();
        String aceessToken = null;
        AccessToken a = null;
        if (extras != null) {
            aceessToken = extras.getString("token");
            Gson gson = new Gson();
            a = gson.fromJson(aceessToken, AccessToken.class);
        } else {
//            sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
//            editor = sharedPreference.edit();
//            sharedPreference.getString("instagramToken", null);
        }

        Context context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        getFacebookFeed(a);


    }

    void getFacebookFeed(AccessToken a) {
        Bundle params = new Bundle();
        params.putString("fields", "picture,likes.limit(10).summary(true),comments.limit(10).summary(true),story,icon,message,place,shares");
        params.putString("limit", "10");

        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
                a,
                "me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "response is " + String.valueOf(response));
                        try {
                            showResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            /* handle the result */
                    }
                }
        ).executeAsync();
    }

    public void showResponse(GraphResponse response) throws JSONException {

        arrayLists = prepareData(response);
        adapter = new FbAdapter(context, arrayLists);
        recyclerView.setAdapter(adapter);
    }

    ArrayList<FbVersion> prepareData(GraphResponse response) throws JSONException {

        ArrayList<FbVersion> dataArray = new ArrayList<>();

        System.out.print(response);
        response.getRawResponse();
        JSONObject object = response.getJSONObject();
        Log.d("OBJECT", String.valueOf(object));
        response.getError();
        //JSONObject objImage = (JSONObject) object.get("data");
        System.out.print("checking");
        JSONArray mediaArray = object.getJSONArray("data");

        JSONArray imgArray = new JSONArray();
        for (int i = 0; i < mediaArray.length(); i++) {
            JSONObject jObj = mediaArray.getJSONObject(i);
            //if (jObj.get("type").equals("image")) {
                imgArray.put(jObj);
            //}
        }

        if (imgArray != null) {
            for (int i = 0; i < imgArray.length(); i++) {
                FbVersion facebookObj = new FbVersion();
                JSONObject jObj = imgArray.getJSONObject(i);
                //JSONObject objImagee = (JSONObject) jObj.get("picture");
                if(jObj.has("message") && !jObj.has("picture")) { // A text-only post eg. a status
                    String message = jObj.getString("message");
                    facebookObj.setMessage(message);
                    dataArray.add(facebookObj);
                }
                if(jObj.has("picture")) { // A picture posted
                    String img_url = jObj.getString("picture");
                    facebookObj.setImage_url(img_url);

                    if(jObj.has("message")) { // Picture has a caption/message with it
                        String message = jObj.getString("message");
                        facebookObj.setMessage(message);
                    }
                    if(jObj.has("story")) {
                        String story = jObj.getString("story");
                        facebookObj.setStory(story);
                    }
                }

                if(jObj.has("likes")) {
                    JSONObject likeObj = jObj.getJSONObject("likes");
                    JSONObject likesArray = likeObj.getJSONObject("summary");
                    String likes = likesArray.getString("total_count");
                    String has_liked = likesArray.getString("has_liked");
                    facebookObj.setLikes(likes);
                    facebookObj.setHas_liked(has_liked);
                }
                if(jObj.has("comments")) {
                    JSONObject commObj = jObj.getJSONObject("comments");
                    JSONObject commArray = commObj.getJSONObject("summary");
                    String comments = commArray.getString("total_count");
                    facebookObj.setComments(comments);
                }
                dataArray.add(facebookObj);
            }
        }
        return dataArray;
    }
}