package com.application.social.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.social.data.UserDetails;
import com.application.social.utils.AfterUpload;
import com.application.social.utils.UploadManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.application.social.views.R.id.signup_button;

//import com.facebook.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , GoogleApiClient.OnConnectionFailedListener , AfterUpload {


    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;


    //gplus login
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 420;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut;
    private Button btnSignUp;
    private Button btnLogIn;
    private Button signupButton;
    private LinearLayout llProfileLayout;
    private LinearLayout linerLayout;
    private LinearLayout linearLayout2;
    private TextView notAMember;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;

    private Button donebutton;

    private LinearLayout linearLayout;

    UploadManager uploadManager = new UploadManager();
    UploadManager uM =new UploadManager();
    UserDetails userDetails =  new UserDetails();
    UserDetails uD = new UserDetails();

    EditText edit_input_name;
    EditText edit_input_email;
    EditText edit_input_pwd;
    EditText edit_login_email;
    EditText edit_login_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.application.social",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        super.onCreate(savedInstanceState);
        UploadManager.addCallback(this);
        initializeControls();
        Button btnStartAnotherActivity;
        btnStartAnotherActivity = (Button) findViewById(R.id.platformActivity);
        btnStartAnotherActivity.setOnClickListener(this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        donebutton = (Button) findViewById(R.id.loginButton); // no visibility
        signupButton = (Button) findViewById(R.id.signup_button);  // no visibility
        linearLayout = (LinearLayout) findViewById(R.id.sign_layout);
        linearLayout2= (LinearLayout) findViewById(R.id.login_layout); // no visibility
        notAMember = (TextView) findViewById(R.id.textView2);
        donebutton.setOnClickListener(this);
        signupButton.setOnClickListener(this);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnLogIn = (Button) findViewById(R.id.signin_button);
        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
        loginWithGoogle();

        edit_input_name   = (EditText)findViewById(R.id.input_name);
        edit_input_email  = (EditText)findViewById(R.id.input_email);
        edit_input_pwd    = (EditText)findViewById(R.id.input_password);
        edit_login_email  = (EditText)findViewById(R.id.login_email);
        edit_login_pwd    = (EditText)findViewById(R.id.login_password);

    }

    private void initializeControls() {
        setContentView(R.layout.activity_main);

        //gpluslogin
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        //btnSignUp.setOnClickListener(this);
    }

    private void loginWithGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
    }

//    @Override
//    protected void onDestroy(){
//
//    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
//                        updateUI(false);
//                        // TODO: 4/20/2017 change this to access token in shared preferences
                        uploadManager.logout("cmlkaGkga3VtYXJpMTQ5MjY4MDk5MzUyODAwMDMyNjA0MA==");
                    }
                });
    }

    private void handleGPlusSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //Fetch values
            userDetails.setToken(acct.getIdToken());
            userDetails.setEmail(acct.getEmail());
            userDetails.setFbGoId(acct.getId());
            userDetails.setName(acct.getDisplayName());
            userDetails.setProfilePic(acct.getPhotoUrl().toString());
            userDetails.setSource(1);
            //send cred to UploadManager to store
            uploadManager.login(userDetails);
            //Set values
            txtName.setText(userDetails.email);
            txtEmail.setText(userDetails.name);
            //Set profile pic with the help of Glide
//            Glide.with(getApplicationContext()).load(userDetails.profilePic)
//                    .thumbnail(0.2f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);


//            updateUI(true);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Login failed" ,Toast.LENGTH_LONG );
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.btn_sign_out:
                signOut();
                break;
            case R.id.platformActivity:
                Intent intent = new Intent(this, Home.class);
//                Intent intent = new Intent(this, Compose.class);
                startActivity(intent);
                break;
            case R.id.loginButton:
                linearLayout.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);
                notAMember.setVisibility(View.GONE);
                break;
            case signup_button:
                linearLayout2.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                notAMember.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_signup:
                // TODO: 5/29/2017 check for validity
                if(edit_input_name.getText().toString()!=null && edit_input_email.getText().toString()!=null && edit_input_pwd.getText().toString()!=null) {
                    uD = new UserDetails();
                    uD.setEmail(edit_input_email.getText().toString());
                    uD.setName(edit_input_name.getText().toString());
                    uD.setPassword(edit_input_pwd.getText().toString());
                    uM = new UploadManager();
                    uM.customSignup(uD);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter the required fields." ,Toast.LENGTH_LONG );
                    toast.show();
                    // TODO: 5/29/2017 send a toast
                }
                break;
            case R.id.signin_button:
                // TODO: 5/29/2017 check for validity
                if(edit_input_email!=null && edit_input_pwd!=null) {
                    uD = new UserDetails();
                    uD.setName(edit_input_name.getText().toString());
                    uD.setPassword(edit_input_pwd.getText().toString());
                    uM = new UploadManager();
                    uM.customSignup(uD);
                }
                else{
                    // TODO: 5/29/2017 send a toast
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter the required fields." ,Toast.LENGTH_LONG );
                    toast.show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGPlusSignInResult(result);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
//
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone() ) {
//            // If the user's cached UserDetailss are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleGPlusSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleGPlusSignInResult(googleSignInResult);
//                }
//            });
//        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        Log.w(TAG, "App stopped");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "App destroyed");

        super.onDestroy();
    }

    @Override
    public void doneLoggingIn(String message) {
        Log.d(TAG,"done uploading");
        if (message == "SUCCESS") {

            Intent intent = new Intent(MainActivity.this, Home.class);
            intent.putExtra("Some_message", "staring new activity");
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(MainActivity.this, "Login Failed." ,Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void doneLoggingIn() {
        Log.d(TAG,"done uploading");
        Toast.makeText(getApplicationContext(), "Login Failed." ,Toast.LENGTH_LONG ).show();
    }
    @Override
    public void doneLoggingOut(String message) {
//        if (message == "SUCCESS") {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            intent.putExtra("Some_message", "staring new activity");
//            startActivity(intent);
//            finish();
//        }
//        else{
//            Toast.makeText(MainActivity.this, "Login Failed." ,Toast.LENGTH_LONG ).show();
//        }
    }
    @Override
    public void doneTwitterLogIn() {
        Log.d(TAG,"twitter stored");
//        Intent intent = new Intent(MainActivity.this, Compose.class);
//        intent.putExtra("Some_message", "staring new activity");
//        startActivity(intent);
    }

    @Override
    public void donePinterestLogIn () {
        Log.d(TAG,"pinterest stored");
//        sharedPreference = getApplicationContext().getSharedPreferences("TokenPreference", 0);
//        editor = sharedPreference.edit();
//        editor.putString("pinterest_login", "true");
//        editor.commit();
//        Intent intent = new Intent(MainActivity.this, Home.class);
//        intent.putExtra("Some_message", "staring new activity");
//        startActivity(intent);
    }

    @Override
    public void doneFacebookLogIn() {
        Log.d(TAG,"facebook stored");
//        Intent intent = new Intent(MainActivity.this, Home.class);
//        intent.putExtra("Some_message", "staring new activity");
//        startActivity(intent);
    }

    @Override
    public void doneInstagramLogIn() {
        Log.d(TAG,"instagram stored");

    }

    @Override
    public void doneTwitterPost() {
        Log.d(TAG,"twitter posted");

    }}