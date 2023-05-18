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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.client.R;
import com.example.client.databinding.FragmentHomeBinding;
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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {


    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;
    private Polyline currentPolyline;
    private List<LatLng> polylinePoints;

    public final String LATITUDE_KEY = "latitude";
    public final String LONGITUDE_KEY = "longitude";

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private GoogleMap googleMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);


        sp = requireContext().getSharedPreferences("spLocation", MODE_PRIVATE);
        edit = sp.edit();



//        //change map type
//        SupportMapFragment mapFragmen = SupportMapFragment.newInstance();
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.map, mapFragmen);
//        fragmentTransaction.commit();
//
//        mapFragmen.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap map) {
//                googleMap = map;
//                // Change map type here
//                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            }
//        });



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
                    // Update the marker position on the map
                    updateCurrentLocationMarker(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        };

        return binding.getRoot();
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

            String latitude_sp = sp.getString(LATITUDE_KEY, "null");
            String longitude_sp = sp.getString(LONGITUDE_KEY, "null");
            Log.d("TAGMainnn", "onCreate: " +"latitude : " + latitude_sp + "  longitude : " + longitude_sp);

            double latitude_sp_double = Double.parseDouble(latitude_sp);
            double longitude_sp_double = Double.parseDouble(longitude_sp);

            // Adjust the initial camera position and zoom level
            double defaultLatitude = latitude_sp_double; // Replace with your desired latitude
            double defaultLongitude = longitude_sp_double; // Replace with your desired longitude
            float defaultZoomLevel = 12f; // Replace with your desired zoom level

            LatLng defaultLocation = new LatLng(defaultLatitude, defaultLongitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, defaultZoomLevel));
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Display a rationale for requesting the permission
            Toast.makeText(requireContext(), "GPS permission is required for live tracking", Toast.LENGTH_SHORT).show();
        }

        // Request the permission
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(requireContext(), "Location permission denied. Live tracking cannot be enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000) // Update location every second
                .setFastestInterval(500); // Set the fastest update interval

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            requestLocationPermission();
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }



    private void updateCurrentLocationMarker(LatLng latLng) {
        if (currentLocationMarker != null) {
            currentLocationMarker.setPosition(latLng);
            // Remove the mMap.animateCamera line to prevent automatic zooming out
        } else {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_bus));
            currentLocationMarker = mMap.addMarker(markerOptions);
        }

        polylinePoints.add(latLng);
        drawFinalPolyline();
    }




    private void drawFinalPolyline() {
        // Set the updated points to the Polyline
        currentPolyline.setPoints(polylinePoints);
    }


}