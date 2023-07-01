package com.example.client.Ui.Fragments.Home;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.client.Model.DriversNumbers;
import com.example.client.R;
import com.example.client.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;

    public final String LATITUDE_KEY_CLIENT = "latitude_client";
    public final String LONGITUDE_KEY_CLIENT = "longitude_client";
    public final String DRIVER_ID_KEY = "driverId_client";

    public final String LONGITUDE_KEY_DRIVER = "longitude_driver";

    public final String LATITUDE_KEY_DRIVER = "latitude_driver";
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    DatabaseReference ref;
    FirebaseFirestore firestore;
    //Polyline getCurrentPolyline;

    DatabaseReference driverLocationRef;
    String driverId_sp;
    double longitude_sp_client, latitude_sp__client, longitude_sp_driver, latitude_sp__driver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        sp = getContext().getSharedPreferences("spLocation", MODE_PRIVATE);
        edit = sp.edit();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final String CLIENT_NUMBER_KEY = "driverNumber";
        int num = sp.getInt(CLIENT_NUMBER_KEY,0);

        Log.d("ClientNumber ", String.valueOf(num));

        longitude_sp_client = sp.getFloat(LONGITUDE_KEY_CLIENT, 0.0f);
        latitude_sp__client = sp.getFloat(LATITUDE_KEY_CLIENT, 0.0f);
        longitude_sp_driver = sp.getFloat(LONGITUDE_KEY_DRIVER, 0.0f);
        latitude_sp__driver = sp.getFloat(LATITUDE_KEY_DRIVER, 0.0f);
        driverId_sp = sp.getString(DRIVER_ID_KEY, "null_id");

        Log.d("HomeFragmentTAG", "onCreateView: client  --->   " + latitude_sp__client + "   --->   " + longitude_sp_client);
        Log.d("HomeFragmentTAG", "onCreateView: driver  --->   " + latitude_sp__driver + "   --->   " + longitude_sp_driver);
        Log.d("HomeFragmentTAG", "onCreateView: driver_id  --->   " + driverId_sp);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Drivers_numbers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DriversNumbers num = document.toObject(DriversNumbers.class);

                        String driver_id = num.getId();
                        Log.d("HomeFragmentTAG", "onComplete: driver_id  --->   " + driver_id);
                        edit.putString(DRIVER_ID_KEY, driver_id);
                        edit.commit();

                    }

                } else {
                    Log.d("LoginActivityLOG", task.getException().getMessage());
                }
            }
        });

        driverLocationRef = FirebaseDatabase.getInstance().getReference("DriverLocation");
//        driverLocationRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // استعراض البيانات المحدثة في Realtime Database
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String driverId = snapshot.getKey();
//                    Double latitudeObj = snapshot.child("latitude").getValue(Double.class);
//                    Double longitudeObj = snapshot.child("longitude").getValue(Double.class);
//
//                    if (latitudeObj != null && longitudeObj != null) {
//                        latitude_sp__driver = latitudeObj.doubleValue();
//                        longitude_sp_driver = longitudeObj.doubleValue();
//
//                        edit.putFloat(LATITUDE_KEY_DRIVER, (float) latitude_sp__driver);
//                        edit.putFloat(LONGITUDE_KEY_DRIVER, (float) longitude_sp_driver);
//                        edit.apply();
//
//                        Toast.makeText(getActivity(), "Latitude: \" + latitude + \", Longitude: \" + longitude", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // خطأ في Realtimee
//                Log.e("LocationService", "Failed to read location from Realtime Database: " + databaseError.getMessage());
//                Toast.makeText(getActivity(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
     String DriverId = sp.getString("DriverId",null);
        if (DriverId != null) {
            driverLocationRef.child(DriverId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        Double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                        if (latitude != null && longitude != null) {
                            latitude_sp__driver = latitude.doubleValue();
                            longitude_sp_driver = longitude.doubleValue();

                            edit.putFloat(LATITUDE_KEY_DRIVER, (float) latitude_sp__driver);
                            edit.putFloat(LONGITUDE_KEY_DRIVER, (float) longitude_sp_driver);
                            edit.apply();

                            Toast.makeText(getActivity(), "Latitude: \" + latitude + \", Longitude: \" + longitude", Toast.LENGTH_SHORT).show();
                        }

                        Log.d("DriverLocation", "Latitude: " + latitude);
                        Log.d("DriverLocation", "Longitude: " + longitude);
                    } else {
                        // المؤشر غير موجود
                        Log.d("DriverLocation", "Index not found");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // خطأ في Realtimee
                    Log.e("DriverLocation", "Failed to read location from Realtime Database: " + databaseError.getMessage());
                    Toast.makeText(getActivity(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



        ActivityResultLauncher<String> arl = registerForActivityResult
                (new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        arl.launch(Manifest.permission.CALL_PHONE);

        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

        return binding.getRoot();
    }

    private void showCustomDialog() {
        // Create an instance of your custom dialog class
        CustomDialogFragment dialog = new CustomDialogFragment();

        // Show the dialog using the fragment manager
        dialog.show(getParentFragmentManager(), "CustomDialog");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // إضافة ماركر الباص
        LatLng busLatLng = new LatLng(latitude_sp__driver, longitude_sp_driver);
        googleMap.addMarker(new MarkerOptions()
                .position(busLatLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_bus))
                .title("Bus"));

        // إضافة ماركر العادي
        LatLng clientlLatLng = new LatLng(latitude_sp__client, longitude_sp_client);
        googleMap.addMarker(new MarkerOptions()
                .position(clientlLatLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("Your Location"));

        PolylineOptions polylineOptions = new PolylineOptions()
                .add(busLatLng, clientlLatLng)
                .width(5)
                .color(Color.BLUE);
        Polyline polyline = googleMap.addPolyline(polylineOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(busLatLng);
        builder.include(clientlLatLng);
        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }
}
