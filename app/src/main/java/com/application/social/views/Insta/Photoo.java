package com.application.social.views.Insta;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.application.social.utils.Instagram.Constants;
import com.application.social.utils.Instagram.InstaImageAdapter;
import com.application.social.utils.Instagram.InstaVersion;
import com.application.social.views.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Photoo extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList arrayLists= new ArrayList<>();
    private InstaImageAdapter adapter ;

    Context context;

    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoo);
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
        getPhotosList(authToken);


    }
    private void getPhotosList(String authToken){
        getInstagramImages(authToken);
    }
    public void getInstagramImages(String authToken){

        JSONArray object = null;
        try {
            Get_Images get_images = new Get_Images(authToken);
            get_images.execute();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
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

    private String urlStringForRecentMediaRequest(String authToken) {

        String apiBaseUrl = Constants.API_BASE_URL;
        String selfMedia = Constants.SELF_MEDIA;

        String strUrl = apiBaseUrl + selfMedia + "access_token=" + authToken;
        return strUrl;
    }

    public class Get_Images extends AsyncTask<String, String,ArrayList<String>> {
        private String token;
        public Get_Images(){

        }
        Get_Images(String token){
            this.token=token;
        }
        private String convertStreamToString(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        protected ArrayList<String> doInBackground(String... params) {

            ArrayList<String> imageArray = new ArrayList<String>();

            try {
//                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpClient httpclient = new DefaultHttpClient();
                String url = urlStringForRecentMediaRequest(token).toString();
                HttpGet httpget = new HttpGet(url);
                httpget.setHeader("Content-Type", "application/json");

                HttpResponse response;

                response = httpclient.execute(httpget);
                int statusCode = response.getStatusLine().getStatusCode();
                String reason = response.getStatusLine().getReasonPhrase();
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result = convertStreamToString(instream);
                    JSONObject object = new JSONObject(new JSONTokener(result));

                    JSONArray mediaArray = object.getJSONArray("data");
                    JSONArray imgArray = new JSONArray();
                    for (int i=0;i<mediaArray.length();i++) {
                        JSONObject jObj = mediaArray.getJSONObject(i);
                        if (jObj.get("type").equals("image")){
                            imgArray.put(jObj);
                        }
                    }

                    if (imgArray!=null){
                        for (int i=0;i<imgArray.length();i++) {
                            JSONObject jObj = imgArray.getJSONObject(i);
                            JSONObject objImage = (JSONObject) jObj.get("images");
                            JSONObject objStdImage = (JSONObject) objImage.get("thumbnail");
                            String img_url = objStdImage.getString("url");
                            imageArray.add(img_url);

                        }
                    }
                    System.out.println(imageArray);
                    instream.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return imageArray;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result)
        {
            showImage(result);
        }
    }

}
