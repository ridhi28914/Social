package com.application.social.utils;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;

import com.application.social.data.UserDetails;
import com.application.social.views.MainActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ridhi on 3/26/2017.
 */

public  class UploadManager {


    String mTAG = "myAsyncTask";

    //static list
    public void callAsync(UserDetails details) {

        StoreCred sc = new StoreCred(details);
        Log.d(mTAG, details.email);
        sc.execute();
    }

    public class StoreCred extends AsyncTask< Void , UserDetails, String> {
        private UserDetails cred;
        MainActivity mainobj = new MainActivity();
        String value;

        public StoreCred() {
        }

        StoreCred(UserDetails cred) {
            this.cred = cred;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {

            Log.d(mTAG, "email is " + cred.token);

            OkHttpClient client;
            client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("email", cred.email)
                    .add("name", cred.name)
                    .add("client_id","social_android_client")
                    .add("app_type","social_android")
                    .add("fbGoId", cred.fbGoId)
                    .add("source", String.valueOf(cred.source))
                    .add("profile_pic", cred.profilePic)
                    .add("token", cred.token)
                    .build();

            String url = "http://192.168.43.22:8080/SocialServer/rest/auth/login";

            try {
                String response= ApiCall.POST(client,url,body);
                Log.d(mTAG, "rspnse is:-" + response);
                return response;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;


        }
        @Override
        protected void onPostExecute(String Jobject) {
            JsonParser pr = new JsonParser();
            Log.d(mTAG,"json object is:- "+ Jobject);
//            Void res = pr.getAccessToken(Jobject);
            mainobj.doneLoggingIn();
//                super.onPostExecute(Jobject);

        }
    }



}
