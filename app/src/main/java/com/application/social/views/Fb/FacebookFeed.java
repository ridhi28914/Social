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

import org.json.JSONException;
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
        AccessToken at=null;
        Gson gson = new Gson();
        if(extras!=null){
            aceessToken = extras.getString("token");
             at = gson.fromJson(aceessToken, AccessToken.class);
        }
        else{
            sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
            editor = sharedPreference.edit();
            aceessToken = sharedPreference.getString("fbToken", null);
            at = gson.fromJson(aceessToken, AccessToken.class);
        }

        Context context=getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        getFacebookFeed(at);


    }
    void getFacebookFeed(AccessToken at){
        Bundle params = new Bundle();
        params.putString("fields", "picture,likes,comments,story,icon,message,place,shares");
        params.putString("limit", "10");

        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
                at,
                "me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "response is "+ String.valueOf(response));
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
        adapter = new InstaImageAdapter(context, arrayLists);
        recyclerView.setAdapter(adapter);
    }

     ArrayList prepareData(GraphResponse response) throws JSONException {

         JSONObject object=response.getJSONObject();
         JSONObject data= object.getJSONObject("data");
         String s=object.getString("data");
         System.out.print(data);
//         response.getError();
         return null;
     }
}
