package com.application.social.views.Insta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.application.social.utils.Instagram.InstaImageAdapter;
import com.application.social.utils.Instagram.InstaVersion;
import com.application.social.utils.Instagram.InstagramInterface;
import com.application.social.utils.Instagram.InstagramListener;
import com.application.social.utils.Instagram.InstagramManager;
import com.application.social.views.Home;
import com.application.social.views.R;

import java.util.ArrayList;


public class Photo extends Activity {

    InstagramManager manager = InstagramManager.getInstance();
    private RecyclerView recyclerView;

    //    private RecyclerView.LayoutManager layoutManager;
    ArrayList arrayLists= new ArrayList<>();
    private InstaImageAdapter adapter ;

    Context context;

    private Button logoutButton;
//    SharedPreferences settings;
//    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Context context=getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayLists.add("https://scontent.cdninstagram.com/t51.2885-15/s150x150/e35/c0.135.1080.1080/18512385_774736376023759_894327120057073664_n.jpg");
        adapter = new InstaImageAdapter(getApplicationContext(),arrayLists );
        Bundle extras = getIntent().getExtras();
//        logoutButton = (Button) findViewById(R.id.btnLogout);

//        gridView = (GridView)findViewById(R.id.gridview);
        context = this;
        String authToken= (String) extras.get("authToken");
        manager.setAccessToken(authToken);

//        recyclerView.setAdapter(adapter);
        getPhotosList();
//        settings = getSharedPreferences(Constants.PREF_NAME, 0);
//        editor = settings.edit();
//        manager.setAccessToken(authToken);
//        getPhotosList();
    }

    public void showImage(ArrayList<String> arrImage){

        System.out.println(arrImage);
        arrayLists = prepareData(arrImage);

        adapter = new InstaImageAdapter(context, arrayLists);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList prepareData(ArrayList<String> arrImage){

        ArrayList arrayList= new ArrayList<>();
        for(int i=0;i<arrImage.size();i++){
            InstaVersion instaVersion = new InstaVersion();
//            androidVersion.setVersion_name(arrImage[i]);
            instaVersion.setImage_url(arrImage.get(i));
            arrayList.add(instaVersion);
        }
        return arrayList;
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
    @Override
    protected void onDestroy(){
        System.out.print("actvity is finished");
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
