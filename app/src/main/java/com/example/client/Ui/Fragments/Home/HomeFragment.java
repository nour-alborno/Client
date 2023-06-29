package com.example.client.Ui.Fragments.Home;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.client.Model.DriversNumbers;
import com.example.client.R;
import com.example.client.databinding.FragmentHomeBinding;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    MarkerOptions driver_marker, client_marker;

    //polyline object
    private List<Polyline> polylines = null;


    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker, driverLocationMarker;
    private Polyline currentPolyline;
    private List<LatLng> polylinePoints;

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

    String driverId_sp;
    double longitude_sp_client, latitude_sp__client, longitude_sp_driver, latitude_sp__driver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);


        sp = requireContext().getSharedPreferences("spLocation", MODE_PRIVATE);
        edit = sp.edit();


        final String CLIENT_NUMBER_KEY = "driverNumber";
        int num = sp.getInt(CLIENT_NUMBER_KEY,0);

        Log.d("ClientNumber ", String.valueOf(num));


        longitude_sp_client = sp.getFloat(LONGITUDE_KEY_CLIENT, 0.0f);
        latitude_sp__client = sp.getFloat(LATITUDE_KEY_CLIENT, 0.0f);
        longitude_sp_driver = sp.getFloat(LONGITUDE_KEY_DRIVER, 0.0f);
        latitude_sp__driver = sp.getFloat(LATITUDE_KEY_DRIVER, 0.0f);
        driverId_sp = sp.getString(DRIVER_ID_KEY, "null_id");

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
                    edit.putFloat(LATITUDE_KEY_DRIVER, (float) latitude_driver);
                    edit.putFloat(LONGITUDE_KEY_DRIVER, (float) longitude_driver);
                    edit.apply();

//                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));}}

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                System.err.println("There was an error getting the GeoFire location: " + databaseError);
                Log.d("HomeFragmentTAG", "onCancelled: There was an error getting the GeoFire location: " + databaseError);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    updateCurrentLocationMarker(new LatLng(latitude_sp__client, longitude_sp_client), new LatLng(latitude_sp__driver, longitude_sp_client));

              //      Toast.makeText(getActivity(), "cliant : " + latitude_sp__client + "  " + longitude_sp_client + "    driver : " + latitude_sp__driver + " " + longitude_sp_driver, Toast.LENGTH_SHORT).show();

                    Log.d("HomeFragmentTAG", "locationCallback: "+"cliant : " + latitude_sp__client + "  " + longitude_sp_client + "    driver : " + latitude_sp__driver + " " + longitude_sp_driver);

                }

//                for (Location location : locationResult.getLocations()) {
//                    // Update the marker position on the map
//                    updateCurrentLocationMarker(new LatLng(location.getLatitude(), location.getLongitude()));
//                }
            }
        };


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
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            polylinePoints = new ArrayList<>();
            PolylineOptions polylineOptions = new PolylineOptions()
                    .width(5)
                    .color(Color.RED);

            currentPolyline = mMap.addPolyline(polylineOptions);

            // Adjust the initial camera position and zoom level
            double defaultLatitude = latitude_sp__client; // Replace with your desired latitude
            double defaultLongitude = longitude_sp_client; // Replace with your desired longitude
            float defaultZoomLevel = 12f; // Replace with your desired zoom level

            LatLng defaultLocation = new LatLng(defaultLatitude, defaultLongitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, defaultZoomLevel));
        }
//        else {
//            requestLocationPermission();
//        }
    }


//    private void requestLocationPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION)) {
//            // Display a rationale for requesting the permission
//            Toast.makeText(requireContext(), "GPS permission is required for live tracking", Toast.LENGTH_SHORT).show();
//        }
//
//        // Request the permission
//        ActivityCompat.requestPermissions(requireActivity(),
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                LOCATION_PERMISSION_REQUEST_CODE);
//    }


    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1000) // Update location every second
                    .setFastestInterval(500); // Set the fastest update interval

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
//        else {
//            requestLocationPermission();
//        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


    private void updateCurrentLocationMarker(LatLng latLng_client, LatLng latLng_driver) {


        if (currentLocationMarker != null) {
            currentLocationMarker.setPosition(latLng_client);
            // Remove the mMap.animateCamera line to prevent automatic zooming out
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng_client, 15f));

        } else {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng_client)
                    .title("your Location");
            currentLocationMarker = mMap.addMarker(markerOptions);
        }



        if (driverLocationMarker != null) {
            driverLocationMarker.setPosition(latLng_driver);
            // Remove the mMap.animateCamera line to prevent automatic zooming out
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng_driver, 15f));
        } else {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng_driver)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_bus));
            driverLocationMarker = mMap.addMarker(markerOptions);
        }

        polylinePoints.add(latLng_client);
        polylinePoints.add(latLng_driver);
        drawFinalPolyline();
    }


    private void drawFinalPolyline() {
        // Set the updated points to the Polyline
        currentPolyline.setPoints(polylinePoints);
    }


}