package com.example.client.Ui.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;
import com.example.client.Ui.Activities.Login.LoginActivity;
import com.example.client.Ui.Activities.onboarding.OnboardingActivity;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String IS_FIRST_KEY = "firstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                edit.putBoolean(IS_FIRST_KEY, false);
                boolean isFirstTime = sp.getBoolean(IS_FIRST_KEY, true);
                if (isFirstTime) {
                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                edit.apply();
                finish();
            }
        }, 4000);
    }
}
