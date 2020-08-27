package com.muradit.projectx.View.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muradit.projectx.Model.LoginSignUpModel;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.Model.others.GetCurrentLocation;
import com.muradit.projectx.R;
import com.muradit.projectx.View.login.loginView;
import com.muradit.projectx.ViewModel.MainActivityViewModel.MainViewModel;
import com.onesignal.OneSignal;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView profileImage;
    private TextView userName;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initiliaize oneSignal
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();




        //init the viewModel
        mainViewModel=new MainViewModel(this);




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        firebaseAuth=FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser()!=null)
          OneSignal.sendTag("User_ID",firebaseAuth.getCurrentUser().getEmail());


        //google init
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        initDrawer(navigationView);
    }

    private void initDrawer(NavigationView navigationView) {
        View headerVew=navigationView.getHeaderView(0);
        profileImage=headerVew.findViewById(R.id.userImage);
        userName=headerVew.findViewById(R.id.username);
        if(firebaseAuth.getCurrentUser()!=null) {


            getUserInfoFromDatabase();


        }


    }


    private void getUserInfoFromDatabase() {

        mainViewModel.getUserInfo().observe(this, new Observer<LoginSignUpModel>() {
            @Override
            public void onChanged(LoginSignUpModel loginSignUpModel) {
                CurrentUserInfo.name=loginSignUpModel.getName();
                CurrentUserInfo.email=loginSignUpModel.getEmail();
                CurrentUserInfo.password=loginSignUpModel.getPassword();
                CurrentUserInfo.id=loginSignUpModel.getId();
                CurrentUserInfo.image=loginSignUpModel.getImage();
                CurrentUserInfo.phone=loginSignUpModel.getPhone();
                CurrentUserInfo.city=loginSignUpModel.getCity();
                CurrentUserInfo.lang=loginSignUpModel.getLang();
                setLanguage(loginSignUpModel.getLang());


                userName.setText(CurrentUserInfo.name);
                Glide.with(MainActivity.this).load(CurrentUserInfo.image).into(profileImage);


                mainViewModel.updateLocation();

            }
        });



    }

    private void setLanguage(String lang) {
       // this flag_language_changed is to prevent the app from infinite loop
        // if it was not there each time user will navigate to MainActivity
        //he will call setLanguage and the Activity will keep restarting

        if(!Flags.flag_language_changed) {
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.setLocale(new Locale(lang.toLowerCase()));
            res.updateConfiguration(conf, dm);
            Flags.flag_language_changed=true;

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(firebaseAuth.getCurrentUser() !=null)
         getMenuInflater().inflate(R.menu.main, menu);
        else
            getMenuInflater().inflate(R.menu.gust_menu, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_loggout:
                signOut();
                break;
            case R.id.login:
                Intent i=new Intent(this,loginView.class);
                startActivity(i);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut(){
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();

        // Google sign out

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "logged out", Toast.LENGTH_SHORT).show();

                    }
                });

        Intent i=new Intent(MainActivity.this,loginView.class);
        startActivity(i);
        finish();

    }

    public void setActionBarTitle(String your_title) {
        getSupportActionBar().setTitle(your_title);
    }
}
