package com.application.social.views;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.application.social.utils.MultiSelectionSpinner;
import com.application.social.utils.MyAlarmService;
import com.application.social.utils.UploadManager;
import com.application.social.views.Fb.FacebookFeed;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
public class Compose extends AppCompatActivity  implements MultiSelectionSpinner.OnMultipleItemsSelectedListener{
    private static final String TAG = "Compose.class";
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    AccessToken accessToken=null;
    MultiSelectionSpinner multiSelectionSpinner;
    protected static TextView displayCurrentTime;
    protected static TextView displayCurrentDate;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
        editor = sharedPreference.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_16px);
        String[] array = {"Facebook", "Twitter"};
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(array);
        multiSelectionSpinner.setListener(this);

        displayCurrentTime = (TextView)findViewById(R.id.selected_time);
        displayCurrentDate = (TextView)findViewById(R.id.selected_date);

//
//        Button buttonStart = (Button)findViewById(R.id.startalarm);
//
////        Button buttonCancel = (Button)findViewById(R.id.cancelalarm);
//        buttonStart.setOnClickListener(new Button.OnClickListener(){
//
//
//        @Override
//        public void onClick(View arg0) {
//
//        Intent myIntent = new Intent(Compose.this, MyAlarmService.class);
//
//        pendingIntent = PendingIntent.getService(Compose.this, 0, myIntent, 0);
//
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        calendar.add(Calendar.SECOND, 10);
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//
//
//
//        Toast.makeText(Compose.this, "Start Alarm", Toast.LENGTH_SHORT).show();
//
//    }});
//        buttonCancel.setOnClickListener(new Button.OnClickListener(){
//
//
//
//            @Override
//
//            public void onClick(View arg0) {
//
//
//                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//
//                alarmManager.cancel(pendingIntent);
//
//
//
//                // Tell the user about what we did.
//
//                Toast.makeText(Compose.this, "Cancel!", Toast.LENGTH_LONG).show();
//
//
//
//
//
//            }});


//        Button displayTimeButton = (Button)findViewById(R.id.select_time);
//        assert displayTimeButton != null;
//        displayTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TimePicker mTimePicker = new TimePicker();
//                mTimePicker.show(getFragmentManager(), "Select time");
//            }
//        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_sendnow:
                postMessage();
                break;
            case R.id.action_schedule:
                    DatePickerFragment mDatePicker = new DatePickerFragment();
                    mDatePicker.show(getFragmentManager(), "Select date");
                    TimePicker mTimePicker = new TimePicker();
                    mTimePicker.show(getFragmentManager(), "Select time");
                    break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void postMessage(){
        EditText et=(EditText)findViewById(R.id.et_message);
        List<Integer> l = multiSelectionSpinner.getSelectedIndices();
        Log.d(TAG,"l is "+l.toString());
        if(et.getText().toString() !=null && (l.contains(0) || l.contains(1)) ) {
            if (l.contains(0) || l.contains(1)) {
                if (l.contains(0)) {
                    String accessToken = sharedPreference.getString("fbToken", null);
                    Gson gson = new Gson();
                    AccessToken at = gson.fromJson(accessToken, AccessToken.class);
                    onFacebookLoginSuccess(at,et.getText().toString());
                }
                if (l.contains(1)) {
                    String twitterSession = sharedPreference.getString("twitterSession", null);
                    Gson gson = new Gson();
                    TwitterSession ts = gson.fromJson(twitterSession, TwitterSession.class);
                    sendTweets(et.getText().toString(), ts);
                }
            }
            Toast.makeText(this, "Message sent.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Please select one of the social networks or message is null", Toast.LENGTH_SHORT).show();
        }


    }
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            displayCurrentDate.setText(String.valueOf(day) + "-" + String.valueOf(month)+ "-" +   String.valueOf(year));
            displayCurrentTime.getText().toString();
            System.out.print(displayCurrentTime.getText().toString());
            System.out.print(displayCurrentDate.getText().toString());
            String toParse =   displayCurrentDate.getText().toString()+" " + displayCurrentTime.getText().toString() ; // Results in "2-5-2012 20:43"
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm"); // I assume d-M, you may refer to M-d for month-day instead.
            Date date = null;
            try {
                date = formatter.parse(toParse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long millis = date.getTime();

            EditText et=(EditText)findViewById(R.id.et_message);
            List<Integer> l = multiSelectionSpinner.getSelectedIndices();
            if(et.getText().toString() !=null && (l.contains(0) || l.contains(1)) ) {

                editor.putString("schedule_message", et.getText().toString());
                if(l.contains(0)) {
                    editor.putString("send_fb", "true");
                }
                else{
                    editor.putString("send_fb", "false");
                }
                if(l.contains(1) ){
                    editor.putString("send_twitter", "true");
                }
                else{
                    editor.putString("send_twitter", "false");
                }

                editor.commit();

                Intent myIntent = new Intent(Compose.this, MyAlarmService.class);
                pendingIntent = PendingIntent.getService(Compose.this, 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                if(millis < Integer.MAX_VALUE)
                    calendar.add(Calendar.SECOND, (int) millis);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(Compose.this, "Start Alarm", Toast.LENGTH_SHORT).show();
                Toast.makeText(Compose.this, "Message sent.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(Compose.this, "Please select one of the social networks or message is null", Toast.LENGTH_SHORT).show();
            }


//            Intent i = new Intent(Compose.this, Posted.class);
//            Bundle extras = new Bundle();
//            extras.putString("token", g);
//            i.putExtras(extras);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(i);
//            finish();
        }
}

    public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            displayCurrentTime.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
    }

    public void sendTweets(String id, TwitterSession session) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<Tweet> update = statusesService.update(id, null, null, null, null, null, null, null, null);
        Log.d("TwitterKit", "Login with Twitter failure");
        update.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Log.e(TAG, "Result<Tweet> result" + result.data.toString());

            }

            @Override
            public void failure(com.twitter.sdk.android.core.TwitterException exception) {
                Log.e(TAG, "Result<Tweet> result" + exception.getMessage());
            }
        });
    }
    public void onFacebookLoginSuccess(AccessToken accessToken,String message) {

        Bundle params = new Bundle();
        params.putString("message", message);
/* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "response is "+ String.valueOf(response));
            /* handle the result */
                    }
                }
        ).executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0 , R.anim.slide_out_right);

//        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_right);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

}
