package com.application.social.views.Pint;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.application.social.data.UserDetails;
import com.application.social.utils.UploadManager;
import com.application.social.views.R;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;
import com.pinterest.android.pdk.Utils;
import com.squareup.picasso.Picasso;

import static com.application.social.views.BuildConfig.DEBUG;

public class PintHome extends TabActivity {
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    UploadManager uploadManager=new UploadManager();;
    private TextView nameTv;
    private ImageView profileIv;
    private final String USER_FIELDS = "id,image,counts,created_at,first_name,last_name,bio";
    PDKUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pint_home);
        nameTv = (TextView) findViewById(R.id.name_textview);
        profileIv = (ImageView) findViewById(R.id.profile_imageview);
        TabHost tabHost = getTabHost();

        TabHost.TabSpec pinsspec = tabHost.newTabSpec("MYPINS");
        // setting Title and Icon for the Tab
        pinsspec.setIndicator("MYPINS");
        Intent photosIntent = new Intent(this, MyPins.class);
        pinsspec.setContent(photosIntent);

        TabHost.TabSpec boardspec = tabHost.newTabSpec("MYBOARDS");
        boardspec.setIndicator("MYBOARDS");
        Intent songsIntent = new Intent(this, MyBoards.class);
        boardspec.setContent(songsIntent);

        TabHost.TabSpec followspec = tabHost.newTabSpec("FOLLOWING");
        followspec.setIndicator("FOLLOWING");
        Intent videosIntent = new Intent(this, FollowingActivity.class);
        followspec.setContent(videosIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(pinsspec); // Adding photos tab
        tabHost.addTab(boardspec); // Adding songs tab
        tabHost.addTab(followspec); // Adding videos tab
        getMe();

    }
    private void  getMe() {
        PDKClient.getInstance().getMe(USER_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                if (DEBUG) log(String.format("status: %d", response.getStatusCode()));
                user = response.getUser();
                sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
                editor = sharedPreference.edit();
                String userId=sharedPreference.getString("user_id",null);
                UserDetails userDetails= new UserDetails();
                userDetails.setUserId(userId);
                userDetails.setName(user.getFirstName());
                userDetails.setProfilePic(user.getImageUrl());
                userDetails.setFbGoId(user.getUid());
 //               savePinterestDb(userDetails);
                setUser();
            }
            @Override
            public void onFailure(PDKException exception) {
                if (DEBUG)  log(exception.getDetailMessage());
                Toast.makeText(PintHome.this, "/me Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void savePinterestDb(UserDetails user) {
//        uploadManager.pinterestLogIn(user);
//    }

    private void setUser() {
        nameTv.setText(user.getFirstName() + " " + user.getLastName());
        Picasso.with(this).load(user.getImageUrl()).into(profileIv);
    }

    private void log(String msg) {
        if (!Utils.isEmpty(msg))
            Log.d(getClass().getName(), msg);
    }

}