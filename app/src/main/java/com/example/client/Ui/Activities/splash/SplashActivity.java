package com.example.client.Ui.Activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.client.Ui.Activities.Login.LoginActivity;
import com.example.client.Ui.Activities.onboarding.OnboardingActivity;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.Ui.base_classes.BaseActivity;
import com.example.client.databinding.ActivitySplashBinding;
import com.google.firebase.database.DatabaseReference;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;
//    SharedPreferences sp;
//    SharedPreferences.Editor edit;
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

//        sp = getSharedPreferences("sp", MODE_PRIVATE);
//        edit = sp.edit();

        checkNewDay();


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


    private void checkNewDay() {


        String previous = sp.getString("JourneyDate",null);
        String driverId = sp.getString("DriverId",null);
        boolean journeyGoingAtendTaken = sp.getBoolean("journeyGoingAtendTaken",false);
        boolean journeyReturnAtendTaken = sp.getBoolean("journeyReturnAtendTaken",false);
        boolean journeyReturnAtendTakenEx = sp.getBoolean("journeyReturnAtendTakenEx",false);
        boolean journeyGoingAtendTakenEx = sp.getBoolean("journeyGoingAtendTakenEx",false);



        if(previous != null && driverId !=null ){

            boolean x = previous.equals(AppUtility.getDate());
            Log.d("boolean",String.valueOf(x));
            if (!x){
                edit.putString("JourneyDate",null);
                if (journeyReturnAtendTaken){
                    edit.putBoolean("journeyReturnAtendTaken",false);
                }

                if (journeyGoingAtendTaken){
                    edit.putBoolean("journeyGoingAtendTaken",false);
                }

                if (journeyGoingAtendTakenEx ){
                    edit.putBoolean("journeyGoingAtendTakenEx",false);
                }
                if (journeyReturnAtendTakenEx){
                    edit.putBoolean("journeyReturnAtendTakenEx",false);
                }
                if (driverId != null){
                    edit.putString("DriverId",null);
                }
                edit.commit();
            }





        }


    }
}
