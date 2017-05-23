package com.application.social.views.Insta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.application.social.utils.Instagram.ImageAdapter;
import com.application.social.utils.Instagram.InstaImageAdapter;
import com.application.social.utils.Instagram.InstaVersion;
import com.application.social.views.Home;
import com.application.social.views.R;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import static com.application.social.views.R.id.listView;


public class Photo extends Activity {

    InstagramManager manager = InstagramManager.getInstance();

    private static GridView gridView;
    private static Context context;

    private Button logoutButton;
//    SharedPreferences settings;
//    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Bundle extras = getIntent().getExtras();

//        settings = getSharedPreferences(Constants.PREF_NAME, 0);
//        editor = settings.edit();

//        logoutButton = (Button) findViewById(R.id.btnLogout);

//        gridView = (GridView)findViewById(R.id.gridview);
        context = this;
        String authToken= (String) extras.get("authToken");
        manager.setAccessToken(authToken);
        getPhotosList();
    }

    private void getPhotosList(){

        manager.getInstagramImages();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_photo_list, menu);
//        return true;
//    }




    public void showImage(ArrayList<String> arrImage){
        System.out.println(arrImage);
//        ImageAdapter adapter = new ImageAdapter(context,R.layout.photo_cell,arrImage);
//        gridView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        initViews(arrImage);
        if(findViewById(R.id.card_recycler_view)==null)
            System.out.print("null");
        else {


            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);

            ArrayList androidVersions = prepareData(arrImage);
            InstaImageAdapter adapter = new InstaImageAdapter(getApplicationContext(), androidVersions);
            recyclerView.setAdapter(adapter);
        }
    }
//    private void initViews(ArrayList<String> arrImage){
//
//
//    }
    private static ArrayList prepareData(ArrayList<String> arrImage){

        ArrayList android_version = new ArrayList<>();
        for(int i=0;i<arrImage.size();i++){
            InstaVersion androidVersion = new InstaVersion();
//            androidVersion.setVersion_name(arrImage[i]);
            androidVersion.setImage_url(arrImage.get(i));
            android_version.add(androidVersion);
        }
        return android_version;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed () {

    }

    public void logoutButtonAction(View v) {

        manager.deleteInstagramCookies();
//        editor.remove("access_token");
//        editor.commit();

        show_main_view();
    }

    private void show_main_view(){
        Intent intent = new Intent(Photo.this, Home.class);
        startActivity(intent);
        finish();
    }
}
