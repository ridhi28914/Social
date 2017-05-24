package com.application.social.views.Fb;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.application.social.utils.Instagram.InstaImageAdapter;
import com.application.social.views.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class FacebookFeed extends AppCompatActivity {
    String TAG = "Facebookfeed class";
    private RecyclerView recyclerView;
    ArrayList arrayLists= new ArrayList<>();
    private InstaImageAdapter adapter ;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_feed);
        
        Bundle extras = getIntent().getExtras();
        String aceessToken =null;
        AccessToken a=null;
        if(extras!=null){
            aceessToken = extras.getString("token");
            Gson gson = new Gson();
             a = gson.fromJson(aceessToken, AccessToken.class);
        }
        else{
//            sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
//            editor = sharedPreference.edit();
//            sharedPreference.getString("instagramToken", null);
        }

        Context context=getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        getFacebookFeed(a);


    }
    void getFacebookFeed(AccessToken a){
        Bundle params = new Bundle();
        params.putString("fields", "picture,likes,comments,story,icon,message,place,shares");
        params.putString("limit", "10");

        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
                a,
                "me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "response is "+ String.valueOf(response));
                        showResponse(response);

            /* handle the result */
                    }
                }
        ).executeAsync();
    }
    public void showResponse(GraphResponse response){

        arrayLists = prepareData(response);
        adapter = new InstaImageAdapter(context, arrayLists);
        recyclerView.setAdapter(adapter);
    }

     ArrayList prepareData(GraphResponse response){

         System.out.print(response);
        response.getRawResponse();
         JSONObject object=response.getJSONObject();
         response.getJSONArray();

         response.getError();
         return null;
     }
}
