package com.example.client.Ui.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.Ui.Activities.Login.LoginActivity;
import com.example.client.Ui.Activities.onboarding.OnboardingActivity;
import com.example.client.databinding.ActivitySplashBinding;
import com.google.firebase.database.DatabaseReference;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    DatabaseReference driverLocationRef;
    String DriverId;
    public static final String IS_FIRST_KEY = "firstTime";

    public final String LONGITUDE_KEY_DRIVER = "longitude_driver";

    public final String LATITUDE_KEY_DRIVER = "latitude_driver";
    Double latitude, longitude;
    double latitude_sp__driver, longitude_sp_driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                edit.putBoolean(IS_FIRST_KEY, false);
                boolean isFirstTime = sp.getBoolean(IS_FIRST_KEY, true);
                if (isFirstTime) {
                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                    finish();

                } else {

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
                edit.apply();
            }
        }, 4000);
    }
}
