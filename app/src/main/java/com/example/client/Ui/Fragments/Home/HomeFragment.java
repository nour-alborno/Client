package com.example.client.Ui.Fragments.Home;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.client.R;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.Ui.Fragments.Attendance.AttendanceFragment;
import com.example.client.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

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
    String DriverId;
    Dialog dialog;
    Double latitude, longitude;
    //Polyline getCurrentPolyline;
    LatLng busLatLng;
    Marker driver_marker;
    private boolean isDataFetched = false;
    DatabaseReference driverLocationRef;
    String driverId_sp;
    double longitude_sp_client;
    double latitude_sp__client;
    double longitude_sp_driver;
    double latitude_sp__driver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        sp = getContext().getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final String CLIENT_NUMBER_KEY = "driverNumber";
        String num = sp.getString(CLIENT_NUMBER_KEY, null);

        Log.d("ClientNumber ", String.valueOf(num));

        longitude_sp_client = sp.getFloat(LONGITUDE_KEY_CLIENT, 0.0f);
        latitude_sp__client = sp.getFloat(LATITUDE_KEY_CLIENT, 0.0f);
        longitude_sp_driver = sp.getFloat(LONGITUDE_KEY_DRIVER, 0.0f);
        latitude_sp__driver = sp.getFloat(LATITUDE_KEY_DRIVER, 0.0f);
       // driverId_sp = sp.getString(DRIVER_ID_KEY, "null_id");

        Log.d("HomeFragmentTAG", "onCreateView: client  --->   " + latitude_sp__client + "   --->   " + longitude_sp_client);
        Log.d("HomeFragmentTAG", "onCreateView: driver  --->   " + latitude_sp__driver + "   --->   " + longitude_sp_driver);
     //   Log.d("HomeFragmentTAG", "onCreateView: driver_id  --->   " + driverId_sp);

        DriverId = sp.getString("DriverId", null);
        firestore = FirebaseFirestore.getInstance();
        Log.d("AttendanceFragmentDID", "Home onComplete: driver_idd  --->   " + DriverId);


        driverLocationRef = FirebaseDatabase.getInstance().getReference("DriverLocation");


        Log.d("AttendanceFragmentDID", "Home onCreateView: " + DriverId);

        if (DriverId != null) {
            driverLocationRef.child(DriverId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        longitude = dataSnapshot.child("longitude").getValue(Double.class);


                        if (latitude != null && longitude != null) {
                            latitude_sp__driver = latitude;
                            longitude_sp_driver = longitude;

                            edit.putFloat(LATITUDE_KEY_DRIVER, (float) latitude_sp__driver);
                            edit.putFloat(LONGITUDE_KEY_DRIVER, (float) longitude_sp_driver);
                            edit.apply();

                            // Update the map if it has already been initialized
                            if (mMap != null) {
                                mMap.clear();
                                onMapReady(mMap);
                            }
                        }

                        isDataFetched = true;
                    } else {
                        Log.d("DriverLocation", "Index not found");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("DriverLocation", "Failed to read location from Realtime Database: " + databaseError.getMessage());
                    AppUtility.showSnackbar(binding.getRoot(), "Error: " + databaseError.getMessage());
                }
            });

        }else if (DriverId != null && (latitude_sp__client != 0.0 && longitude_sp_client != 0.0)){
            showAttendanceLocationDialog();
        }else if (latitude_sp__client == 0.0 && longitude_sp_client == 0.0){
            showLocationDialog();
        }


        ActivityResultLauncher<String> arl = registerForActivityResult
                (new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                           // AppUtility.showSnackbar(binding.getRoot(), "Call Permission granted");
                                Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                        } else {
                            AppUtility.showSnackbar(binding.getRoot(), "Call Permission denied");
                            // Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        arl.launch(Manifest.permission.CALL_PHONE);

        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
                AppUtility.vibrateButtonClicked(getActivity());
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


        Log.d("AttendanceFragmentDID", "Home onMapReady sp div2 : " + latitude_sp__driver + " , " + latitude_sp__driver);

        Log.d("AttendanceFragmentDID", "Home onMapReady long lat div2  :  " + latitude + " , " + longitude);

        Log.d("AttendanceFragmentDID", "Home onMapReady cla : " + latitude_sp__client + " , " + longitude_sp_client);


        // Check if latitude_sp__driver and longitude_sp_driver are not null or 0.0
        if (latitude_sp__driver != 0.0 && longitude_sp_driver != 0.0 && DriverId != null) {
            // Add marker for bus location
            LatLng busLatLng = new LatLng(latitude_sp__driver, longitude_sp_driver);
            googleMap.addMarker(new MarkerOptions()
                    .position(busLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_bus))
                    .title("Bus"));

            // Add marker for client location
            LatLng clientlLatLng = new LatLng(latitude_sp__client, longitude_sp_client);
            googleMap.addMarker(new MarkerOptions()
                    .position(clientlLatLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("Your Location"));

            // Draw polyline and adjust camera position
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
        } else {
            // Only add marker for client location if bus location is not available
            LatLng clientlLatLng = new LatLng(latitude_sp__client, longitude_sp_client);
            googleMap.addMarker(new MarkerOptions()
                    .position(clientlLatLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("Your Location"));

            // Move camera to client location
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clientlLatLng, 15));
        }



    }
    private void showAttendanceLocationDialog() {
        dialog = new Dialog(getActivity());
        View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.item_dialog_get_attendence, requireActivity().findViewById(R.id.custom_dialog));
        dialog.setContentView(dialogView);
        dialog.show();

       Button btn_getAttendance = dialogView.findViewById(R.id.btn_goAttendence);
        btn_getAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceFragment attendanceFragment = new AttendanceFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, attendanceFragment); // Replace R.id.container with the ID of the container layout in your XML
                transaction.addToBackStack(null); // This allows the user to navigate back to AFragment
                transaction.commit();

                AppUtility.vibrateButtonClicked(getActivity());
                dialog.dismiss();
            }
        });
    }

    private void showLocationDialog() {
        dialog = new Dialog(getActivity());
        View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.itm_dialog_take_location, requireActivity().findViewById(R.id.custom_dialog));
        dialog.setContentView(dialogView);
        dialog.show();

        Button btn_getLocation = dialogView.findViewById(R.id.btn_goAttendence);
        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.vibrateButtonClicked(getActivity());
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DriverId = sp.getString("DriverId", null);
    }
}
