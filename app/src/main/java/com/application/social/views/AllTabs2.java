package com.application.social.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AllTabs2 extends FragmentActivity {

    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(AllTabs2.this, button1);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int position = item.getOrder();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(android.R.id.content,(Fragment) item.getTitle());
                        // Locate Position
//                        switch (position) {
//                            case 0:
//                                ft.replace(android.R.id.content, (Fragment) item.getTitle());
//                                break;
//                            case 1:
//                                FragmentTransaction replace1 = ft.replace(android.R.id.content, (Fragment) item.getTitle());
//                                break;
//                            case 2:
//                                FragmentTransaction replace2 = ft.replace(android.R.id.content, (Fragment) item.getTitle());
//                                break;
//                        }
                        ft.commit();

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method
    }
}
