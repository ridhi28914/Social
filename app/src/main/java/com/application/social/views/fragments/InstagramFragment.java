package com.application.social.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.social.utils.Instagram.Constants;
import com.application.social.utils.Instagram.InstaImageAdapter;
import com.application.social.utils.Instagram.InstaVersion;
import com.application.social.views.Insta.Photoo;
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

/**
 * Created by Harsh on 31-05-2017.
 */

public class InstagramFragment extends BaseFragment {

    private RecyclerView recyclerView;
    ArrayList<InstaVersion> arrayLists = new ArrayList<>();
    private InstaImageAdapter adapter ;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    private View getView;
    private Context context;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_photoo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView = getView();
        context = getContext();
        activity = getActivity();

        recyclerView = (RecyclerView) getView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        Bundle extras = activity.getIntent().getExtras();
        String authToken=null;

            sharedPreference = context.getSharedPreferences("TokenPreference", 0);
            editor = sharedPreference.edit();
            authToken = sharedPreference.getString("instagramToken", null);
//        logoutButton = (Button) findViewById(R.id.btnLogout);

//        gridView = (GridView)findViewById(R.id.gridview);
        getPhotosList(authToken);
    }

    private void getPhotosList(String authToken){
        getInstagramImages(authToken);
    }
    public void getInstagramImages(String authToken){

        JSONArray object = null;
        try {
            InstagramFragment.Get_Images get_images = new InstagramFragment.Get_Images(authToken);
            get_images.execute();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void showImage(ArrayList<InstaVersion> arrImage){

        System.out.println(arrImage);
        arrayLists = prepareData(arrImage);

        adapter = new InstaImageAdapter(context, arrayLists);
        recyclerView.setAdapter(adapter);
    }
    private ArrayList<InstaVersion> prepareData(ArrayList<InstaVersion> arrImage){

        ArrayList<InstaVersion> arrayList= new ArrayList<>();
        for(int i=0;i<arrImage.size();i++){
            InstaVersion instaVersion = new InstaVersion();
            instaVersion.setCaption(arrImage.get(i).getCaption());
            instaVersion.setUser_name(arrImage.get(i).getUser_name());
            instaVersion.setImage_url(arrImage.get(i).getImage_url());
            instaVersion.setProfile_picture(arrImage.get(i).getProfile_picture());
            instaVersion.setLikes(arrImage.get(i).getLikes());
            instaVersion.setComments(arrImage.get(i).getComments());

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

    public class Get_Images extends AsyncTask<InstaVersion, String,ArrayList<InstaVersion>> {
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

        protected ArrayList<InstaVersion> doInBackground(InstaVersion... params) {

            //ArrayList< ArrayList<String> > dataArray = new ArrayList<ArrayList<String>>();
            ArrayList<String> imageArray = new ArrayList<>();
            ArrayList<InstaVersion> dataArray = new ArrayList<>();

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
                            InstaVersion instaObj = new InstaVersion();
                            JSONObject jObj = imgArray.getJSONObject(i);
                            JSONObject objImage = (JSONObject) jObj.get("images");
                            JSONObject objStdImage = (JSONObject) objImage.get("standard_resolution");
                            String img_url = objStdImage.getString("url");

                            JSONObject objName = (JSONObject) jObj.get("user");
                            String username = objName.getString("username");

                            if(!jObj.isNull("caption")) {
                                JSONObject objCaption = (JSONObject) jObj.get("caption");
                                String caption = objCaption.getString("text");
                                instaObj.setCaption(caption);
                            }

                            if(!jObj.isNull("likes")) {
                                JSONObject objLikes = (JSONObject) jObj.get("likes");
                                String likes = objLikes.getString("count");
                                instaObj.setLikes(likes);
                            }

                            if(!jObj.isNull("comments")) {
                                JSONObject objComments = (JSONObject) jObj.get("comments");
                                String comments = objComments.getString("count");
                                instaObj.setComments(comments);
                            }

                            JSONObject objProfile = (JSONObject) jObj.get("user");
                            String profilePictue = objProfile.getString("profile_picture");

                            instaObj.setImage_url(img_url);
                            instaObj.setUser_name(username);
                            instaObj.setProfile_picture(profilePictue);

                            dataArray.add(instaObj);
                        }
                    }
                    System.out.println(imageArray);
                    instream.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return dataArray;
        }


        @Override
        protected void onPostExecute(ArrayList<InstaVersion> result)
        {
            showImage(result);
        }
    }

}
