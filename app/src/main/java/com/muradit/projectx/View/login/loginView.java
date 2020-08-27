package com.muradit.projectx.View.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.GetCurrentLocation;
import com.muradit.projectx.R;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.View.signUp.SignUpView;
import com.muradit.projectx.ViewModel.login.loginViewModel;

public class loginView extends AppCompatActivity {

    private EditText email,password;
    private Button login,openLoginLayout;
    private TextView signUp;
    private loginViewModel loginViewModel;
    private FirebaseAuth firebaseAuth;
    private LoginButton loginButton;
    private SignInButton googleBtn;
  //  private VideoView videoView;

    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    //private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=101;
    private String TAG="google sign";
    private GoogleSignInClient mGoogleSignInClient;
    private TextView skip;
    private LinearLayout loginLay;
    private SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        email=findViewById(R.id.mail);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        openLoginLayout=findViewById(R.id.openEmailLay);
        signUp=findViewById(R.id.createTxt);
        skip=findViewById(R.id.skipTxt);
        loginLay=findViewById(R.id.signWithEmail);
        loginLay.setVisibility(View.GONE);
     //   videoView=findViewById(R.id.mVideoView);
        firebaseAuth=FirebaseAuth.getInstance();
        googleBtn=findViewById(R.id.sign_in_button);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginViewModel= ViewModelProviders.of(this).get(loginViewModel.class);

        //video background code
      /*  Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.login_video);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });*/




        // skip text on click handling
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(loginView.this, MainActivity.class);
                startActivity(i);
            }
        });



        //signUp ON click handling
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(loginView.this, SignUpView.class);
                startActivity(i);
            }
        });

        //login onClick handling
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifiedInput();

            }
        });

        openLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginLay.setVisibility(View.VISIBLE);
                openLoginLayout.setVisibility(View.GONE);
            }
        });

        //facebook code>>>>>>>>>>>>>>
        callbackManager=CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());

        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                final String accessToken = loginResult.getAccessToken().getToken();

                // save accessToken to SharedPreference
            //    addFacebookUser(loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code

            }
        });

        //end of facebook code >>>>>>>>>>


        // GOOGLE LOGIN CODE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }



    private void verifiedInput() {
        if(!TextUtils.isEmpty(email.getText().toString())){
            if(!TextUtils.isEmpty(password.getText().toString()))
                verifiedUser(email.getText().toString(),password.getText().toString());
            else
                password.setError("required");

        }else{
            email.setError("required");
        }
    }

    private void verifiedUser(String email, String password) {
        loginViewModel.init(this);
        loginViewModel.verifiedUser(email,password);
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       callbackManager.onActivityResult(requestCode, resultCode, data);//facebook

       //google
       if (requestCode == RC_SIGN_IN) {
           Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
           try {
               // Google Sign In was successful, authenticate with Firebase
               GoogleSignInAccount account = task.getResult(ApiException.class);
               Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
               Toast.makeText(this, "redirecting...", Toast.LENGTH_SHORT).show();
              // loginViewModel.firebaseAuthWithGoogle(account.getId());
               firebaseAuthWithGoogle(account.getIdToken());
           } catch (ApiException e) {
               // Google Sign In failed, update UI appropriately
               Log.w(TAG, "Google sign in failed", e);
               // [START_EXCLUDE]
              // updateUI(null);
               // [END_EXCLUDE]
           }
       }

    }

    //for google sign in
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
       if(firebaseAuth.getCurrentUser() !=null){
           GetCurrentLocation location=new GetCurrentLocation(this);
           location.getLocation();
           location.getLocationState().observe(this, new Observer<Boolean>() {
               @Override
               public void onChanged(Boolean aBoolean) {
                  if (aBoolean) {
                      Intent i = new Intent(loginView.this, MainActivity.class);
                      startActivity(i);
                  }
                  }
           });

       }
    }


   public void handleFacebookAccessToken(AccessToken token) {

    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
    firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
        @Override
        public void onSuccess(AuthResult authResult) {

            final String email;
            final String phone;
            if(authResult.getUser().getEmail()==null)
                email="no email";
            else
                email=authResult.getUser().getEmail();

            if(authResult.getUser().getPhoneNumber()==null)
                phone="no phone";
            else
                phone=authResult.getUser().getPhoneNumber();


            final String id=authResult.getUser().getUid();
            final String name=authResult.getUser().getDisplayName();
            final String image=authResult.getUser().getPhotoUrl().toString();
            CurrentUserInfo.id=id;
            GetCurrentLocation getCurrentLocation=new GetCurrentLocation(loginView.this);
            getCurrentLocation.getLocation();
            getCurrentLocation.getLocationState().observe(loginView.this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    loginViewModel.init(loginView.this);
                    loginViewModel.addUserToDatabase(id,email,name,"no pass",phone,image);

                    Intent i=new Intent(loginView.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });


        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

        }
    });
}

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                final String email;
                final String phone;
                if(authResult.getUser().getEmail()==null)
                    email="no email";
                else
                    email=authResult.getUser().getEmail();

                if(authResult.getUser().getPhoneNumber()==null)
                    phone="no phone";
                else
                    phone=authResult.getUser().getPhoneNumber();


                final String id=authResult.getUser().getUid();
                final String name=authResult.getUser().getDisplayName();
                final String image=authResult.getUser().getPhotoUrl().toString();
                CurrentUserInfo.id=id;

                GetCurrentLocation getCurrentLocation=new GetCurrentLocation(loginView.this);
                getCurrentLocation.getLocation();
                getCurrentLocation.getLocationState().observe(loginView.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        loginViewModel.init(loginView.this);
                        loginViewModel.addUserToDatabase(id,email,name,"no pass",phone,image);

                        Intent i=new Intent(loginView.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getCurrentLocation(){
        GetCurrentLocation location=new GetCurrentLocation(this);
        location.getLocation();
    }



}
