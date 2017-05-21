package com.application.social.utils;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.application.social.data.UserDetails;
import com.application.social.views.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.application.social.utils.CommonLib.SERVER_URL;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.GraphRequest.TAG;

/**
 * Created by Ridhi on 3/26/2017.
 */

public  class UploadManager {

    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;


    String mTAG = "myAsyncTask";

    //static list

    public void login(UserDetails details) {

        LoginCred loginCred = new LoginCred(details);
        Log.d(mTAG, "email is"+details.email);
        loginCred.execute();
    }


        public class LoginCred extends AsyncTask< Void , UserDetails, String> {
            private UserDetails cred;
            MainActivity mainobj = new MainActivity();
            String value;

            public LoginCred() {
            }

            LoginCred(UserDetails cred) {
                this.cred = cred;
            }

            @Override
            protected String doInBackground(Void... params) {

                if(cred.getEmail()==null)
                        cred.setEmail("null");
                if(cred.getFacebookData()==null)
                        cred.setFacebookData("null");
                OkHttpClient client;
                client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("email", cred.email)
                        .add("name", cred.name)
                        .add("client_id", "social_android_client")
                        .add("app_type", "social_android")
                        .add("fbGoId", cred.fbGoId)
                        .add("source", String.valueOf(cred.source))
                        .add("profile_pic", cred.profilePic)
                        .add("token", cred.token)
                        .add("facebook_data", cred.facebookData)
                        .build();

                String url = SERVER_URL+"auth/login";
                String response=null;
                try {
                    response = ApiCall.POST(client, url, body);
                    return response;

                } catch (IOException e) {
                    e.printStackTrace();
//                // TODO: 4/20/2017 return json exception response
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String response) {
                Log.d(mTAG, "response object is:- " + response);
                String accessToken=null;
                String userId=null;
                if (response != null) {
                    JSONObject jObject;
                    try {
                        jObject = new JSONObject(response);
                        JSONObject data = new JSONObject(jObject.getString("response"));
                        Log.d(mTAG, "data is:-" + data);
                        accessToken = data.getString("access_token");
                        userId = data.getString("user_id");
                        Log.d(mTAG, "at is:-" + accessToken);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
                    editor = sharedPreference.edit();
                    editor.putString("access_token", accessToken);
                    editor.putString("user_id",userId);
                    editor.commit();
                    if (sharedPreference.contains("access_token")) {
                        accessToken = sharedPreference.getString("access_token", null);
                        if (accessToken != null) {
                            Log.d(mTAG, "accessToken is :- " + accessToken);
                        } else {
                            Log.d(mTAG, "accessToken is null");
                        }
                    }
                    mainobj.doneLoggingIn();

                }
            }


        }

    public void facebookLogIn(UserDetails details) {

        FacebookLogin facebookLogin= new FacebookLogin(details);
        Log.d(mTAG, "user is"+details.getName());
        facebookLogin.execute();
    }
    public class FacebookLogin extends AsyncTask< Void , UserDetails, String> {
        private UserDetails cred;
        MainActivity mainobj = new MainActivity();
        String value;

        public FacebookLogin() {
        }

        FacebookLogin(UserDetails cred) {
            this.cred = cred;
        }

        @Override
        protected String doInBackground(Void... params) {

            if(cred.getEmail()==null)
                cred.setEmail("null");
            if(cred.getFacebookData()==null)
                cred.setFacebookData("null");
            OkHttpClient client;
            client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("user_id", cred.userId)
                    .add("email", cred.email)
                    .add("name", cred.name)
                    .add("client_id", "social_android_client")
                    .add("app_type", "social_android")
                    .add("fbGoId", cred.fbGoId)
                    .add("source", String.valueOf(cred.source))
                    .add("profile_pic", cred.profilePic)
                    .add("token", cred.token)
                    .add("facebook_data", cred.facebookData)
                    .build();

            String url = SERVER_URL+"facebook/login";
            String response=null;
            try {
                response = ApiCall.POST(client, url, body);
                return response;

            } catch (IOException e) {
                e.printStackTrace();
//                // TODO: 4/20/2017 return json exception response
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            Log.d(mTAG, "response object is:- " + response);
            sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
            editor = sharedPreference.edit();
            mainobj.doneFacebookLogIn();
//                super.onPostExecute(Jobject);

        }
    }



    public void twitterLogIn(UserDetails details) {

        TwitterLogin twitterLogin= new TwitterLogin(details);
        Log.d(mTAG, "user is"+details.getName());
        twitterLogin.execute();
    }
    public class TwitterLogin extends AsyncTask< Void , UserDetails, String> {
        private UserDetails cred;
        MainActivity mainobj = new MainActivity();
        String value;

        public TwitterLogin() {
        }

        TwitterLogin(UserDetails cred) {
            this.cred = cred;
        }

        @Override
        protected String doInBackground(Void... params) {

            OkHttpClient client;
            client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("user_id", cred.userId)
                    .add("name", cred.name)
                    .add("client_id", "social_android_client")
                    .add("app_type", "social_android")
                    .add("fbGoId", cred.fbGoId)
                    .add("token", cred.token)
                    .build();

            String url = SERVER_URL+"twitter/login";
            String response=null;
            try {
                response = ApiCall.POST(client, url, body);
                return response;

            } catch (IOException e) {
                e.printStackTrace();
//                // TODO: 4/20/2017 return json exception response
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            Log.d(mTAG, "response object is:- " + response);
            sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
            editor = sharedPreference.edit();
            mainobj.doneTwitterLogIn();
//                super.onPostExecute(Jobject);

        }
    }

    public void pinterestLogIn(UserDetails details) {

        PinterestLogin pinterestLogin= new PinterestLogin(details);
        Log.d(mTAG, "user is"+details.getUserId());
        pinterestLogin.execute();
    }
    public class PinterestLogin extends AsyncTask< Void , UserDetails, String> {
        private UserDetails cred;
        MainActivity mainobj = new MainActivity();
        String value;

        public PinterestLogin() {
        }

        PinterestLogin(UserDetails cred) {
            this.cred = cred;
        }

        @Override
        protected String doInBackground(Void... params) {

            OkHttpClient client;
            client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("user_id", cred.userId)
                    .add("name", cred.name)
                    .add("client_id", "social_android_client")
                    .add("app_type", "social_android")
                    .add("fbGoId", cred.fbGoId)
                    .add("profile_pic", cred.profilePic)
                    .build();

            String url = SERVER_URL+"pinterest/login";
            String response=null;
            try {
                response = ApiCall.POST(client, url, body);
                return response;

            } catch (IOException e) {
                e.printStackTrace();
//                // TODO: 4/20/2017 return json exception response
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            Log.d(mTAG, "response object is:- " + response);
            mainobj.donePinterestLogIn();
//                super.onPostExecute(Jobject);

        }
    }



    public void logout(String accessToken){
        LogoutCred loc=new LogoutCred();
        loc.execute(accessToken);
    }

    public class LogoutCred extends AsyncTask< String , String, String> {


        @Override
        protected String doInBackground(String... accessToken) {
            Log.d(mTAG,"access token is"+accessToken[0]);
            OkHttpClient client;
            client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("access_token", accessToken[0])
                    .add("client_id","social_android_client")
                    .add("app_type","social_android")
                    .build();

            String url = SERVER_URL+"auth/logout";

            try {
                String response= ApiCall.POST(client,url,body);
                Log.d(mTAG, "rspnse is:-" + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                //                // TODO: 4/20/2017 return json exception response
//                Log.d(mTAG,"stack trace is :"+ e.printStackTrace());
                return null;
            }
        }
        @Override
        protected void onPostExecute(String Jobject) {
            sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
//            sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
            editor = sharedPreference.edit();
            editor.remove("access_token");
            editor.commit();
            Log.d(mTAG,"removed preference:- ");
        }
    }
}
