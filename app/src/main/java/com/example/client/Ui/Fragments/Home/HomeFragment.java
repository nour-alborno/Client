package com.example.client.Ui.Fragments.Home;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.SharedPreferences;
import android.location.Location;
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
import com.example.client.databinding.FragmentHomeBinding;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng origin;
    private LatLng destination;

    //current and destination location objects
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;


    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;
    private Polyline currentPolyline;
    private List<LatLng> polylinePoints;

    public final String LATITUDE_KEY_CLIENT = "latitude_client";
    public final String LONGITUDE_KEY_CLIENT = "longitude_client";
    public final String DRIVER_ID_KEY = "driverId_client";

    public final String LONGITUDE_KEY_DRIVER = "longitude_driver";

    public final String LATITUDE_KEY_DRIVER = "latitude_driver";
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private GoogleMap googleMap;
    DatabaseReference ref;
    FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);




        sp = requireContext().getSharedPreferences("spLocation", MODE_PRIVATE);
        edit = sp.edit();

        double longitude_sp_client = sp.getFloat(LONGITUDE_KEY_CLIENT,  0.0f);
        double latitude_sp__client = sp.getFloat(LATITUDE_KEY_CLIENT,0.0f);
        double longitude_sp_driver = sp.getFloat(LONGITUDE_KEY_DRIVER, 0.0f);
        double latitude_sp__driver = sp.getFloat(LATITUDE_KEY_DRIVER, 0.0f);
        String driverId_sp = sp.getString(DRIVER_ID_KEY, "null_id");

        Log.d("HomeFragmentTAG", "onCreateView: client  --->   " + longitude_sp_client + "   --->   " + latitude_sp__client);
        Log.d("HomeFragmentTAG", "onCreateView: driver  --->   " + longitude_sp_driver + "   --->   " + latitude_sp__driver);
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


        ref = FirebaseDatabase.getInstance().getReference("DriverLocation");
        GeoFire geoFire = new GeoFire(ref);

        geoFire.getLocation(driverId_sp, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    double longitude_driver = location.longitude;
                    double latitude_driver = location.latitude;
                    edit.putFloat(LATITUDE_KEY_DRIVER, (float) longitude_driver);
                    edit.putFloat(LONGITUDE_KEY_DRIVER, (float) latitude_driver);
                    edit.apply();
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });


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
        dialog.show(getChildFragmentManager(), "CustomDialog");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

























}