package com.example.client.Ui.Activities.Main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.client.R;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.Ui.Fragments.Attendance.AttendanceFragment;
import com.example.client.Ui.Fragments.Home.HomeFragment;
import com.example.client.Ui.Fragments.Profile.ProfileFragment;
import com.example.client.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainView, AttendanceFragment.onMove {

    ActivityMainBinding binding;

    Dialog dialog;
    Button btn_getLocation;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String la, lo, latitude_sp, longitude_sp;
    public final String LATITUDE_KEY_CLIENT = "latitude_client";
    public final String LONGITUDE_KEY_CLIENT = "longitude_client";
    AlertDialog alertDialog;
    public final String CLIENT_ID_KEY = "clientId";
    String clientId;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private LocationRequest locationRequest;
    MainPresenter MP;
    private static final String DIALOG_SHOWN_KEY = "dialog_shown";
    FirebaseFirestore firestore;
    double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore=FirebaseFirestore.getInstance();

        MP = new MainPresenter(this);
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();



        double longitude_sp = sp.getFloat(LONGITUDE_KEY_CLIENT,  0.0f);
        double latitude_sp = sp.getFloat(LATITUDE_KEY_CLIENT,0.0f);

        if (longitude_sp == 0.0 && latitude_sp == 0.0){
            showLocationDialog();
        }

        Toast.makeText(this, ""+latitude_sp+"     "+longitude_sp, Toast.LENGTH_SHORT).show();





        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);




        MP.AddingFrag(new ProfileFragment());


        binding.bottomNavigationMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                MP.SelectingNavIcon(item);

                return true;
            }
        });


    }

    @Override
    public void onSetFragment(Fragment fragment) {
        //         هنا مجرد ما يفتح التطبيق حيكون الديفولت ع الفراقمنت يلي تحت ، ف حطيت تاعي مجرد ما يجهز تاعك الهوم ي نور
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
        //        وهان بدي اياه يحدد ع الأيقون يلي تحت تاعت السيكيدجول
        binding.bottomNavigationMain.setSelectedItemId(R.id.page_profile);
    }

    @Override
    public void onSelectedNavIcon(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }






    private void showLocationDialog() {
        dialog = new Dialog(MainActivity.this);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dialog, findViewById(R.id.custom_dialog));
        dialog.setContentView(dialogView);
        dialog.show();

        btn_getLocation = dialogView.findViewById(R.id.btn_getLocation);
        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
                AppUtility.vibrateButtonClicked(getBaseContext());
                dialog.dismiss();
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();


//                                        la = String.valueOf(latitude);
//                                        lo = String.valueOf(longitude);

//                                        GeoPoint geoPoint = new GeoPoint(longitude, latitude);
//
//                                        Map<String , Object> setLocation = new HashMap<>();
//                                        setLocation.put("location",geoPoint);

                                        saveLocation(latitude,longitude);

                                        edit.putFloat(LATITUDE_KEY_CLIENT, (float) latitude);
                                        edit.putFloat(LONGITUDE_KEY_CLIENT, (float) longitude);
                                        edit.apply();
                                        Log.d("MainActivityTAG", "if  Location: "+"------------------->      "+"Latitude: " + latitude + "\n" + "Longitude: " + longitude);

                                        Toast.makeText(MainActivity.this, "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    AppUtility.showSnackbar(binding.getRoot(),"GPS is already tured on");

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }



    void saveLocation(double lat , double longi){
        Log.d("MainActivityTAG", "SaveLocationMethod");
        if (lat != 0 && longi != 0) {

            Log.d("MainActivityTAG", "SaveLocationMethodInside");

//            float latitude = sp.getFloat(LATITUDE_KEY_CLIENT,0.0f);
//            float longitude = sp.getFloat(LONGITUDE_KEY_CLIENT,0.0f);
//            edit.apply();

            //Storing location in benf firestore
            GeoPoint geoPoint = new GeoPoint(lat, longi);
            Log.d("MainActivityTAG", "gwoLocation: "+"------------------->      "+"Latitude: " + lat + "\n" + "Longitude: " + longi);


            Map<String , Object> setLocation = new HashMap<>();
            setLocation.put("location",geoPoint);

            clientId = sp.getString(CLIENT_ID_KEY,null);
            firestore.collection("Beneficiaries").document(clientId).update(setLocation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Log.d("MainActivityTAG", "Successful");
                            } else {
                                Log.d("MainActivityTAG", task.getException().getMessage());         }
                        }
                   });
        }
    }

    @Override
    public void move() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        //        وهان بدي اياه يحدد ع الأيقون يلي تحت تاعت السيكيدجول
        binding.bottomNavigationMain.setSelectedItemId(R.id.page_home);

    }
}