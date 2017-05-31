package com.application.social.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import com.application.social.utils.ActionBar;
import android.os.Bundle;
import android.widget.ListView;

import com.application.social.utils.NavListAdapter;

public class AllTabs extends FragmentActivity {

    ListView list;
    NavListAdapter adapter;
    String[] title;
    String[] subtitle;
    int[] icon;
    Fragment fragment1 = new Fragment1();
    Fragment fragment2 = new Fragment2();
    Fragment fragment3 = new Fragment3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Generate title
        title = new String[]{"Title Fragment 1", "Title Fragment 2",
                "Title Fragment 3"};

        // Generate subtitle
        subtitle = new String[]{"Subtitle Fragment 1", "Subtitle Fragment 2",
                "Subtitle Fragment 3"};

        // Generate icon
//        icon = new int[] { R.drawable.action_about, R.drawable.action_settings,
//                R.drawable.collections_cloud };

        // Pass results to NavListAdapter Class
        adapter = new NavListAdapter(this, title, subtitle, icon);

        // Hide the ActionBar Title
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Create the Navigation List in your ActionBar
       ActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Listen to navigation list clicks
        ActionBar.OnNavigationListener navlistener = new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                FragmentTransaction ft = getSupportFragmentManager()
                        .beginTransaction();
                // Locate Position
                switch (position) {
                    case 0:
                        ft.replace(android.R.id.content, fragment1);
                        break;
                    case 1:
                        ft.replace(android.R.id.content, fragment2);
                        break;
                    case 2:
                        ft.replace(android.R.id.content, fragment3);
                        break;
                }
                ft.commit();
                return true;
            }

        };
        // Set the NavListAdapter into the ActionBar Navigation
        //getSupportActionBar().setListNavigationCallbacks(adapter, navlistener);
    }

//    public ActionBar getSupportActionBar() {
//        return supportActionBar;
//    }

//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                      Bundle savedInstanceState);
}

//    private Button button1;
//    Fragment fragment1 = new Fragment1();
//    Fragment fragment2 = new Fragment2();
//    Fragment fragment3 = new Fragment3();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all_tabs);
//
//        //Spinner spinner = (Spinner) findViewById(R.id.platforms_spinner);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        //ArrayAdapter<CharSequence> adapter =
//          //      ArrayAdapter.createFromResource(this, R.array.platforms_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////// Apply the adapter to the spinner
////        spinner.setAdapter(adapter);
////
////        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
////                // An item was selected. You can retrieve the selected item using
////                // parent.getItemAtPosition(pos)
////                final Intent intent;
////                switch(pos){
////                    case 0: setContentView(R.layout.activity_all_tabs);
////                        //intent = new Intent(AllTabs.this, TwitterFeed.class);
//////                        startActivity(intent);
////                        break;
////                    case 1:
////                        setContentView(R.layout.activity_fragments_view);
////                        //intent = new Intent(AllTabs.this, TwitterFeed.class);
////                        //startActivity(intent);
////                        break;
////                    case 2:
////                        setContentView(R.layout.activity_fragments_view);
////                        //intent = new Intent(AllTabs.this, TwitterHome.class);
////                        //startActivity(intent);
////                        break;
////                }
////                //startActivity(intent);
////
////            }
////            public void onNothingSelected(AdapterView<?> parent) {
////                // Another interface callback
////            }
////        });
//        button1 = (Button) findViewById(R.id.button1);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Creating the instance of PopupMenu
//                PopupMenu popup = new PopupMenu(AllTabs.this, button1);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater()
//                        .inflate(R.menu.popup_menu, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        return false;
//                    }
//
//                    //public boolean onMenuItemClick(MenuItem item) {
//                    public boolean OnItemSelectedListener(int pos, long itemId) {
//                        FragmentTransaction ft = getSupportFragmentManager()
//                                .beginTransaction();
//                        // Locate Position
//                        switch (pos) {
//                            case 0:
//                                ft.replace(android.R.id.content, fragment1);
//                                break;
//                            case 1:
//                                ft.replace(android.R.id.content, fragment2);
//                                break;
//                            case 2:
//                                ft.replace(android.R.id.content, fragment3);
//                                break;
//                        }
//                        ft.commit();
//                        return true;
//
//                        }
//                    });
//
//                popup.show(); //showing popup menu
//            }
//        }); //closing the setOnClickListener method
//    }
//}
