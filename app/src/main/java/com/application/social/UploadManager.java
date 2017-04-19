package com.application.social;

import android.os.AsyncTask;
import android.util.Log;

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
    void callAsync(UserDetails details) {

        StoreCred sc = new StoreCred(details);
        Log.d(mTAG, details.email);
        sc.execute();
    }

    public class StoreCred extends AsyncTask< Void , UserDetails, JSONObject> {
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
        protected JSONObject doInBackground(Void... params) {

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
                Response response= ApiCall.POST(client,url,body);
                System.out.print(response.body());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
//            try {
//                JSONObject Jobject = new JSONObject(response);
//                return Jobject;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            return null;
//            }
//            Request request = new Request.Builder()
//                    .url("")
//                    .post(body)
//                    .build();
//            try {
//                response = client.newCall(request).execute();
//               Log.d(TAG,"response is"+response);
//                String jsonobject = response.body().string();
//                JSONObject Jobject = new JSONObject(jsonobject);
//                return Jobject;
////                return response.body();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return null;
//            }

        }
            //            super.doInBackground();


        @Override
        protected void onPostExecute(JSONObject Jobject) {
            JsonParser pr = new JsonParser();
            Log.d(mTAG,"json object is:- "+ Jobject);
//            Void res = pr.getAccessToken(Jobject);
//            mainobj.doneUpload();
                super.onPostExecute(Jobject);

        }
    }
}
//            try {
////                UserDetails ud;
//                URL url = new URL("http://192.168.43.22:8080/SocialServer/rest/auth/login");
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("GET");
//                con.connect();
//                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                value = bf.readLine();
//                return cred;
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//             finally {
//                con.disconnect();
//            }
//            return null;
//        }

