package com.deepak.localtravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class ActivitySplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIMEOUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    SharedPreferences sharePref = getSharedPreferences("user_login_details", Context.MODE_PRIVATE);
                    Boolean value = sharePref.getBoolean("active", Boolean.parseBoolean(""));

                    if(value == true){
                        Intent intentMainScreen = new Intent(ActivitySplashScreen.this, ActivityPunePage.class);
                        startActivity(intentMainScreen);
                        ActivitySplashScreen.this.finish();
                    }
                    else{
                        Intent intentMainScreen = new Intent(ActivitySplashScreen.this, ActivityMainLogin.class);
                        startActivity(intentMainScreen);
                        ActivitySplashScreen.this.finish();
                    }


                }
                catch (Exception e) {
                    e.printStackTrace();
                }



            }
        }, SPLASH_SCREEN_TIMEOUT);

    }
}
