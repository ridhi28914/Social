package com.application.social.views.Insta;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Instagram.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Instagram#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Instagram extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private RecyclerView recyclerView;
    ArrayList<InstaVersion> arrayLists = new ArrayList<>();
    private InstaImageAdapter adapter ;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    Context context;
    private OnFragmentInteractionListener mListener;

    public Instagram() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Instagram.
     */
    // TODO: Rename and change types and number of parameters
    public static Instagram newInstance(String param1, String param2) {
        Instagram fragment = new Instagram();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }
    @Override
    public  void onActivityCreated (Bundle savedInstanceState) {

        Context context=getActivity();

        recyclerView = (RecyclerView) getView().findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        String authToken=null;
        sharedPreference = getActivity().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        authToken = sharedPreference.getString("instagramToken", null);
        getPhotosList(authToken);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instagram, container, false);
    }
    private void getPhotosList(String authToken){
        getInstagramImages(authToken);
    }
    public void getInstagramImages(String authToken){

        JSONArray object = null;
        try {
            Instagram.Get_Images get_images = new Instagram.Get_Images(authToken);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
