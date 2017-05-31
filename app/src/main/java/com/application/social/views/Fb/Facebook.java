package com.application.social.views.Fb;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.social.utils.Facebook.FbAdapter;
import com.application.social.utils.Facebook.FbVersion;
import com.application.social.views.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Facebook.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Facebook#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Facebook extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    private String mParam1;
//    private String mParam2;
    String TAG = "Facebook fragment ";
    private RecyclerView recyclerView;
    ArrayList arrayLists = new ArrayList<>();
    private FbAdapter adapter;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    Context context;

    private OnFragmentInteractionListener mListener;

    public Facebook() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment Facebook.
     */
    public static Facebook newInstance() {
        Facebook fragment = new Facebook();
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
    public  void onActivityCreated (Bundle savedInstanceState){

        String aceessToken = null;
        AccessToken at=null;
        Gson gson = new Gson();
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        aceessToken = sharedPreference.getString("fbToken", null);
        at = gson.fromJson(aceessToken, AccessToken.class);
        Context context = getApplicationContext();

        recyclerView = (RecyclerView) getView().findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        getFacebookFeed(at);
    }
    void getFacebookFeed(AccessToken at) {
        Bundle params = new Bundle();
        params.putString("fields", "picture,likes.limit(10).summary(true),comments.limit(10).summary(true),story,icon,message,place,shares");
        params.putString("limit", "10");

        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
                at,
                "me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        Log.d(TAG, "response is " + String.valueOf(response));
                        try {
                            showResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            /* handle the result */
                    }
                }
        ).executeAsync();
    }

    public void showResponse(GraphResponse response) throws JSONException {

        arrayLists = prepareData(response);
        adapter = new FbAdapter(context, arrayLists);
        recyclerView.setAdapter(adapter);
    }

    ArrayList<FbVersion> prepareData(GraphResponse response) throws JSONException {

        ArrayList<FbVersion> dataArray = new ArrayList<>();

        System.out.print(response);
        response.getRawResponse();
        JSONObject object = response.getJSONObject();
        Log.d("OBJECT", String.valueOf(object));
        response.getError();
        //JSONObject objImage = (JSONObject) object.get("data");
        System.out.print("checking");
        JSONArray mediaArray = object.getJSONArray("data");

        JSONArray imgArray = new JSONArray();
        for (int i = 0; i < mediaArray.length(); i++) {
            JSONObject jObj = mediaArray.getJSONObject(i);
            //if (jObj.get("type").equals("image")) {
            imgArray.put(jObj);
            //}
        }

        if (imgArray != null) {
            for (int i = 0; i < imgArray.length(); i++) {
                FbVersion facebookObj = new FbVersion();
                JSONObject jObj = imgArray.getJSONObject(i);
                //JSONObject objImagee = (JSONObject) jObj.get("picture");
                if(jObj.has("message") && !jObj.has("picture")) { // A text-only post eg. a status
                    String message = jObj.getString("message");
                    facebookObj.setMessage(message);
                    dataArray.add(facebookObj);
                }
                if(jObj.has("picture")) { // A picture posted
                    String img_url = jObj.getString("picture");
                    facebookObj.setImage_url(img_url);

                    if(jObj.has("message")) { // Picture has a caption/message with it
                        String message = jObj.getString("message");
                        facebookObj.setMessage(message);
                    }
                    if(jObj.has("story")) {
                        String story = jObj.getString("story");
                        facebookObj.setStory(story);
                    }
                }

                if(jObj.has("likes")) {
                    JSONObject likeObj = jObj.getJSONObject("likes");
                    JSONObject likesArray = likeObj.getJSONObject("summary");
                    String likes = likesArray.getString("total_count");
                    String has_liked = likesArray.getString("has_liked");
                    facebookObj.setLikes(likes);
                    facebookObj.setHas_liked(has_liked);
                }
                if(jObj.has("comments")) {
                    JSONObject commObj = jObj.getJSONObject("comments");
                    JSONObject commArray = commObj.getJSONObject("summary");
                    String comments = commArray.getString("total_count");
                    facebookObj.setComments(comments);
                }
                dataArray.add(facebookObj);
            }
        }
        return dataArray;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facebook, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
