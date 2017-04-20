package com.application.social.utils;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.application.social.data.UserDetails;
import com.application.social.views.MainActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        Log.d(mTAG, details.email);
        loginCred.execute();
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

            String url = "http://192.168.43.22:8080/SocialServer/rest/auth/logout";

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

                Log.d(mTAG, "email is " + cred.token);

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
                        .build();

                String url = "http://192.168.43.22:8080/SocialServer/rest/auth/login";

                try {
                    String response = ApiCall.POST(client, url, body);
                    Log.d(mTAG, "rspnse is:-" + response);
                    return response;

                } catch (IOException e) {
                    e.printStackTrace();
//                // TODO: 4/20/2017 return json exception response
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String Jobject) {
//            JsonParser pr = new JsonParser();
                Log.d(mTAG, "json object is:- " + Jobject);
                if (Jobject != null) {
                    Log.d(TAG, "Access token retrieved:" + Jobject);
                    sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
                    editor = sharedPreference.edit();
                    editor.putString("access_token", Jobject);
                    editor.commit();
                    if (sharedPreference.contains("access_token")) {
                        String accessToken = sharedPreference.getString("access_token", null);
                        if (accessToken != null) {
                            Log.d(mTAG, "accessToken is :- " + accessToken);
                        } else {
                            Log.d(mTAG, "accessToken is null");
                        }
                    }
//            Void res = pr.getAccessToken(Jobject);
                    mainobj.doneLoggingIn();
//                super.onPostExecute(Jobject);

                }
            }


        }
}
