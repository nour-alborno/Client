package com.example.client.Ui.Fragments.Attendance;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.client.Model.AttendanceConfirmation;
import com.example.client.Model.Benf_Schedule;
import com.example.client.Model.DriverProfile;
import com.example.client.Model.JourneyModel;
import com.example.client.R;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.Ui.base_classes.BaseFragment;
import com.example.client.databinding.FragmentAttendanceBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AttendanceFragment extends BaseFragment {

    DatabaseReference reference;

    String  driverId;
    String journeyIdGoing;
    String journeyIdReturn;


    public final String CLIENT_ID_KEY = "clientId";


    FirebaseFirestore firestore;
    FirebaseDatabase db;
    public final String LATITUDE_KEY_CLIENT = "latitude_client";
    public final String LONGITUDE_KEY_CLIENT = "longitude_client";

    ColorStateList colorStateList;

    public interface onMove{
        void move();
    }

    onMove OnMove;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAttendanceBinding binding;

    public AttendanceFragment() {
        // Required empty public constructor
    }


    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttendanceBinding.inflate(inflater,container,false);
        colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.gray2));
           //getting the journey of today
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        String id = sp.getString(CLIENT_ID_KEY,null);
        edit = sp.edit();



         firestore = FirebaseFirestore.getInstance();
         db =FirebaseDatabase.getInstance();



        Log.d("Day is",  AppUtility.getToday() );

        svaingTakingAttendanceState();

        //Fetching user journeys
        firestore.collection("Benf_Schedule").document(id)
                .collection(AppUtility.getToday()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Benf_schedule","task :"+ task.getResult().toObjects(Benf_Schedule.class));

                            if (!task.getResult().toObjects(Benf_Schedule.class).isEmpty()){
                                binding.layoutJourneyReturn.setVisibility(View.VISIBLE);
                                binding.layoutJourneyStart.setVisibility(View.VISIBLE);

                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.d("Benf_schedule","document :"+ document.toString());
                                    Benf_Schedule benf_schedule = document.toObject(Benf_Schedule.class);



                                    //fetching going journey data
                                    firestore.collection("Journey").document(benf_schedule.getJourneyIdGoing()).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()){

                                                        journeyIdGoing = benf_schedule.getJourneyIdGoing();


                                                        JourneyModel journeyModel = task.getResult().toObject(JourneyModel.class);
                                                        binding.tvStartTimeItemSchedule.setText(journeyModel.getStart());
                                                        binding.tvArrivalsTimeItemSchedule.setText(journeyModel.getEnd());
                                                        binding.tvArrivalsPlace.setText(journeyModel.getOrganization());
                                                        binding.tvStartingPlace.setText(journeyModel.getRegion());




                                                        binding.tvDate.setText(AppUtility.getDate());

                                                        edit.putString("driverIdGoing",journeyModel.getDriver());

                                                        //Fetching DriverName

//                                                             binding.tvDriverName.setText(gettingDriversName(journeyModel.getDriver()));

                                                        firestore.collection("Driver").document(journeyModel.getDriver()).get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        Log.d("Driver", task.getResult().toString());
                                                                        if (task.isSuccessful()) {
                                                                            DriverProfile driverProfile = task.getResult().toObject(DriverProfile.class);

                                                                            binding.tvDriverName.setText(driverProfile.getName());

                                                                            Log.d("driver_name", driverProfile.getName());
                                                                        } else {
                                                                            Log.d("driver_name", task.getException().getMessage());
                                                                        }
                                                                    }
                                                                });




                                                        Log.d("journey",journeyModel.getOrganization());
                                                    } else {
                                                        Log.d("OnFaliure_journey",task.getException().getMessage());
                                                    }
                                                }
                                            });



                                    // fetching return journey data

                                    firestore.collection("Journey").document(benf_schedule.getJourneyIdReturn()).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()){


                                                        journeyIdReturn = benf_schedule.getJourneyIdReturn();


                                                        JourneyModel journeyModel = task.getResult().toObject(JourneyModel.class);
                                                        binding.tvStartTimeItemSchedule2.setText(journeyModel.getStart());
                                                        binding.tvArrivalsTimeItemSchedule2.setText(journeyModel.getEnd());
                                                        binding.tvArrivalsPlace2.setText(journeyModel.getOrganization());
                                                        binding.tvStartingPlace2.setText(journeyModel.getRegion());





                                                        binding.tvDate2.setText(AppUtility.getDate());

                                                        driverId = journeyModel.getDriver();

                                                        edit.putString("driverIdReturn",journeyModel.getDriver());


                                                        //Fetching DriverName

                                                        //binding.tvDriverName2.setText(gettingDriversName(journeyModel.getDriver()));

                                                        firestore.collection("Driver").document(journeyModel.getDriver()).get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        Log.d("Driver", task.getResult().toString());
                                                                        if (task.isSuccessful()) {
                                                                            DriverProfile driverProfile = task.getResult().toObject(DriverProfile.class);

                                                                            binding.tvDriverName2.setText(driverProfile.getName());

                                                                            Log.d("driver_name", driverProfile.getName());
                                                                        } else {
                                                                            Log.d("driver_name", task.getException().getMessage());
                                                                        }
                                                                    }
                                                                });




                                                        Log.d("journey",journeyModel.getOrganization());
                                                    } else {
                                                        Log.d("OnFaliure_journey",task.getException().getMessage());
                                                    }
                                                }
                                            });



                                    Log.d("Benf_schedule", benf_schedule.getJourneyIdGoing());




                                }
                            }




                        } else {
                            Log.d("Benf_schedule", task.getException().getMessage());
                        }
                    }
                });



        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.vibrateButtonClicked(getActivity());
                takingAttendance(id,journeyIdGoing);
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnExclude.setVisibility(View.GONE);
                binding.btnGoMap.setVisibility(View.VISIBLE);

                edit.putBoolean("journeyGoingAtendTaken",true);
                edit.putString("JourneyDate",AppUtility.getDate());
                edit.commit();
                Log.d("AttendanceFragmentDID", "onClick: 1 "+sp.getString("driverIdGoing",null));


            }
        });

        binding.btnGoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.vibrateButtonClicked(getActivity());
                edit.putString("DriverId",sp.getString("driverIdGoing",null));
                edit.putString("JourneyDate",AppUtility.getDate());
                edit.commit();
                OnMove.move();
            }
        });

        binding.btnAccept2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppUtility.vibrateButtonClicked(getActivity());
                takingAttendance(id,journeyIdReturn);

                binding.btnAccept2.setVisibility(View.GONE);
                binding.btnExclude2.setVisibility(View.GONE);
                binding.btnGoMap2.setVisibility(View.VISIBLE);

                edit.putBoolean("journeyReturnAtendTaken",true);
                edit.putString("JourneyDate",AppUtility.getDate());
                edit.commit();

                Log.d("AttendanceFragmentDID", "onClick: 2 "+sp.getString("driverIdReturn",null));

            }
        });

        binding.btnGoMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.vibrateButtonClicked(getActivity());
                edit.putString("DriverId",sp.getString("driverIdReturn",null));

                edit.commit();
                OnMove.move();

            }
        });

       binding.btnExclude.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AppUtility.vibrateNegative(getActivity());
               edit.putBoolean("journeyReturnAtendTakenEx",true);
               edit.commit();

               binding.btnExclude.setEnabled(false);
               binding.btnAccept.setEnabled(false);



               binding.btnAccept.setBackgroundTintList(colorStateList);
               binding.btnExclude.setBackgroundTintList(colorStateList);


               AppUtility.showSnackbar(binding.getRoot(),"You Have Excluded Your Attendance");

           }
       });

        binding.btnExclude2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.vibrateNegative(getActivity());
                edit.putBoolean("journeyGoingAtendTakenEx",true);
                edit.commit();
                binding.btnExclude2.setEnabled(false);
                binding.btnAccept2.setEnabled(false);

                binding.btnAccept2.setBackgroundTintList(colorStateList);
                binding.btnExclude2.setBackgroundTintList(colorStateList);

                AppUtility.showSnackbar(binding.getRoot(),"You Have Excluded Your Attendance");
            }
        });



        return binding.getRoot();




}

    private void svaingTakingAttendanceState() {
        if (sp.getBoolean("journeyGoingAtendTaken",false) ){
            binding.btnGoMap.setVisibility(View.VISIBLE);
            binding.btnAccept.setVisibility(View.GONE);
            binding.btnExclude.setVisibility(View.GONE);
        }
        if (sp.getBoolean("journeyReturnAtendTaken",false)) {
            binding.btnGoMap2.setVisibility(View.VISIBLE);
            binding.btnAccept2.setVisibility(View.GONE);
            binding.btnExclude2.setVisibility(View.GONE);
        }
        if (sp.getBoolean("journeyReturnAtendTakenEx",false)){
            binding.btnAccept.setEnabled(false);
            binding.btnExclude.setEnabled(false);
            binding.btnAccept.setBackgroundTintList(colorStateList);
            binding.btnExclude.setBackgroundTintList(colorStateList);
        }
        if (sp.getBoolean("journeyGoingAtendTakenEx",false)){
            binding.btnAccept2.setEnabled(false);
            binding.btnExclude2.setEnabled(false);
            binding.btnAccept2.setBackgroundTintList(colorStateList);
            binding.btnExclude2.setBackgroundTintList(colorStateList);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        OnMove = (onMove) context;
    }

    void takingAttendance(String userId, String journeyId){
        ArrayList<String> attend = new ArrayList<>();
        attend.add(userId);

        AttendanceConfirmation confirmation = new AttendanceConfirmation();
        confirmation.setUserId(userId);
        confirmation.setLat(sp.getFloat(LATITUDE_KEY_CLIENT, 0.0f));
        confirmation.setLon(sp.getFloat(LONGITUDE_KEY_CLIENT, 0.0f));

        reference = db.getReference("AttendanceConfirmation");
        reference.child(AppUtility.getDate()).child(driverId)
                .child(journeyId).push().setValue(confirmation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("test2","successful");
                        } else {
                            Log.d("test2",task.getException().getMessage());
                        }
                    }
                });
    }






}