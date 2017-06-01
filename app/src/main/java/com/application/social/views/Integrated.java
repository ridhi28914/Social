package com.application.social.views;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.application.social.utils.AfterUpload;
import com.application.social.utils.UploadManager;
import com.application.social.views.fragments.FacebookFragment;
import com.application.social.views.fragments.InstagramFragment;
import com.application.social.views.fragments.TwitterFragment;
import com.application.social.views.fragments.PinterestFragment;

import static android.R.id.message;
import static com.twitter.sdk.android.Twitter.logOut;

public class Integrated extends AppCompatActivity implements View.OnClickListener,AfterUpload {

    Button button1;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    FacebookFragment facebookFragment;
    InstagramFragment instagramFragment;
    TwitterFragment twitterFragment;
    PinterestFragment pinterestFragment;
    String fragmentNumberOld;
    String fragmentNumberNew;
    UploadManager uploadManager = new UploadManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        UploadManager.addCallback(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrated);

        facebookFragment = new FacebookFragment();
        instagramFragment = new InstagramFragment();
        twitterFragment = new TwitterFragment();
        pinterestFragment =new PinterestFragment();

        button1 = (Button) findViewById(R.id.button_dropdown);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((View.OnClickListener) Integrated.this);

        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        fragmentNumberOld= sharedPreference.getString("fragmentNumberOld", null);
        fragmentNumberNew=sharedPreference.getString("fragmentNumberNew",null);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(fragmentNumberOld.equalsIgnoreCase("0")) {
            if (fragmentNumberNew.equalsIgnoreCase("101") )
            {
                transaction.add(R.id.fragment_container, facebookFragment);
                button1.setText("Facebook");
                editor.putString("fragmentNumberOld", "101");
            }
            else if (fragmentNumberNew.equalsIgnoreCase("102"))
            {
                transaction.add(R.id.fragment_container, instagramFragment);
                button1.setText("Instagram");
                editor.putString("fragmentNumberOld", "102");

            }
            else if(fragmentNumberNew=="103")
            {
                transaction.add(R.id.fragment_container, pinterestFragment);
                button1.setText("Pinterest");
                editor.putString("fragmentNumberOld", "103");
            }
            else if(fragmentNumberNew.equalsIgnoreCase("104"))
            {
                transaction.add(R.id.fragment_container, twitterFragment);
                button1.setText("Twitter");
                editor.putString("fragmentNumberOld","104");
            }

            transaction.commit();
            editor.commit();
        }
        else{
            if (fragmentNumberOld.equalsIgnoreCase("101") ) {
                transaction.add(R.id.fragment_container, facebookFragment);
                button1.setText("Facebook");
                editor.putString("fragmentNumberOld", "101");
            } else if (fragmentNumberOld.equalsIgnoreCase("102")) {
                transaction.add(R.id.fragment_container, instagramFragment);
                button1.setText("Instagram");
                editor.putString("fragmentNumberOld", "102");

            }
            else if(fragmentNumberOld=="103")
            {
                transaction.add(R.id.fragment_container, pinterestFragment);
                button1.setText("Pinterest");
                editor.putString("fragmentNumberOld", "103");
            }
            else if(fragmentNumberOld.equalsIgnoreCase("104"))
            {
                transaction.add(R.id.fragment_container, twitterFragment);
                button1.setText("Twitter");
                editor.putString("fragmentNumberOld","104");
            }

            transaction.commit();
            editor.commit();
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Integrated.this, button1);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        func(item);
                        return true;
                    }
                });

                popup.show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.fab:
                startActivity(new Intent(Integrated.this, Compose.class));
                overridePendingTransition(R.anim.slide_in_right, 0);
                break;

        }
    }

    void func(MenuItem item) {
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String choice = (String) item.getTitle();

        fragmentNumberOld= sharedPreference.getString("fragmentNumberOld", null);
        if(fragmentNumberOld.equalsIgnoreCase("101"))
            transaction.remove(facebookFragment);
        else if(fragmentNumberOld.equalsIgnoreCase("102"))
            transaction.remove(instagramFragment);
            else if(fragmentNumberNew=="103")
                transaction.remove(pinterestFragment);
        else if(fragmentNumberOld=="104")
            transaction.remove(twitterFragment);

        transaction.commit();
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();

        if (choice.equalsIgnoreCase("Facebook"))
        {
            transaction2.add(R.id.fragment_container, facebookFragment);
            button1.setText("Facebook");
            editor.putString("fragmentNumberOld","101");
            transaction2.commit();
            editor.commit();
        }
        else if (choice.equalsIgnoreCase("Twitter"))
        {
//            // TODO: 6/1/2017 calltwitter activity
            transaction2.add(R.id.fragment_container, twitterFragment);
            button1.setText("Twitter");
            editor.putString("fragmentNumberOld","104");
            transaction2.commit();
            editor.commit();
        }
        else if (choice.equalsIgnoreCase("Instagram"))
        {
            transaction2.add(R.id.fragment_container, instagramFragment);
            button1.setText("Instagram");
            editor.putString("fragmentNumberOld","102");
            transaction2.commit();
            editor.commit();
        }
        else if (choice.equalsIgnoreCase("Pinterest")) {
            transaction2.add(R.id.fragment_container, pinterestFragment);
                editor.putString("fragmentNumberOld","103");
            transaction2.commit();
            editor.commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }
    private void logOut() {

        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        String at= sharedPreference.getString("access_token", null);
        if(at!=null){
            uploadManager.logout(at);
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doneLoggingIn() {

    }

    @Override
    public void doneLoggingIn(String message) {

    }

    @Override
    public void doneLoggingOut(String message) {
        if (message == "SUCCESS") {
            Intent intent = new Intent(Integrated.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(Integrated.this, "Logout Failed." ,Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void doneTwitterLogIn() {

    }

    @Override
    public void donePinterestLogIn() {

    }

    @Override
    public void doneFacebookLogIn() {

    }

    @Override
    public void doneInstagramLogIn() {

    }

    @Override
    public void doneTwitterPost() {

    }
}

