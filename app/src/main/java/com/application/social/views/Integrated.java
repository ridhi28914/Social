package com.application.social.views;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.application.social.views.fragments.FacebookFragment;
import com.application.social.views.fragments.InstagramFragment;
import com.application.social.views.fragments.TwitterFragment;

public class Integrated extends AppCompatActivity {

    Button button1;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    FacebookFragment facebookFragment;
    InstagramFragment instagramFragment;
    TwitterFragment twitterFragment;
    String fragmentNumberOld;
    String fragmentNumberNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrated);

        facebookFragment = new FacebookFragment();
        instagramFragment = new InstagramFragment();
        twitterFragment = new TwitterFragment();

        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        fragmentNumberOld= sharedPreference.getString("fragmentNumberOld", null);
        fragmentNumberNew=sharedPreference.getString("fragmentNumberNew",null);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(fragmentNumberOld=="0"){
            if(fragmentNumberNew=="101")
                transaction.add(R.id.fragment_container, facebookFragment);
            else if(fragmentNumberNew=="102")
                transaction.add(R.id.fragment_container, instagramFragment);
//            else if(fragmentNumberNew=="103")
//                transaction.add(R.id.fragment_container, pinterestFragment);
            else if(fragmentNumberNew=="104")
                transaction.add(R.id.fragment_container, twitterFragment);

            transaction.commit();
        }
        else{

            if(fragmentNumberNew=="101")
                transaction.remove(facebookFragment);
            else if(fragmentNumberNew=="102")
                transaction.remove(instagramFragment);
//            else if(fragmentNumberNew=="103")
//                transaction.remove(pinerestFragment);
            else if(fragmentNumberNew=="104")
                transaction.remove(twitterFragment);
            transaction.commit();

            if(fragmentNumberNew=="101"){

                transaction.add(R.id.fragment_container, facebookFragment);
            }
            else if(fragmentNumberNew=="102")
            {
                transaction.add(R.id.fragment_container, facebookFragment);
            }
            else if(fragmentNumberNew=="103")
            {
                transaction.add(R.id.fragment_container, facebookFragment);
            }
            else if(fragmentNumberNew=="104")
            {
                transaction.add(R.id.fragment_container, facebookFragment);
            }



        }


        /*button1 = (Button) findViewById(R.id.button1);
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

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method*/
    }

    void func(MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String choice = (String) item.getTitle();
        int pos = 0;
        if (choice == "Facebook")
            pos = 1;
        else if (choice == "Twitter")
            pos = 2;
        else if (choice == "Instagram")
            pos = 3;
        else if (choice == "Pinterest")
            pos = 4;
        switch (pos) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }
}

