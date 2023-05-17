package com.example.client.Ui.Fragments.Attendance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.client.Model.Benefeciares;
import com.example.client.Model.Benf_Schedule;
import com.example.client.Model.DriverProfile;
import com.example.client.Model.JourneyModel;
import com.example.client.R;
import com.example.client.databinding.FragmentAttendanceBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceFragment extends Fragment {




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        FragmentAttendanceBinding binding = FragmentAttendanceBinding.inflate(inflater,container,false);

           //getting the journey of today


        Format f = new SimpleDateFormat("EEEE");
        String day = f.format(new Date());

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();



        Log.d("Day is",  day );

        //Fetching going journey to the user
        firestore.collection("Benf_Schedule").document("1")
                .collection("Tuesday").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Benf_schedule","task :"+ task.getResult().toObjects(Benf_Schedule.class));
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("Benf_schedule","document :"+ document.toString());
                                Benf_Schedule benf_schedule = document.toObject(Benf_Schedule.class);

                                Toast.makeText(getActivity(), benf_schedule.getJourneyIdGoing(), Toast.LENGTH_LONG).show();


                                Log.d("Benf_schedule", benf_schedule.getJourneyIdGoing());


                                //fetching going journey data
                                firestore.collection("Journey").document(benf_schedule.getJourneyIdGoing()).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                         if (task.isSuccessful()){
                                                      JourneyModel journeyModel = task.getResult().toObject(JourneyModel.class);
                                                      binding.tvStartTimeItemSchedule.setText(journeyModel.getStart());
                                                      binding.tvArrivalsTimeItemSchedule.setText(journeyModel.getEnd());
                                                      binding.tvArrivalsPlace.setText(journeyModel.getOrganization());
                                                      binding.tvStartingPlace.setText(journeyModel.getRegion());


                                                             SimpleDateFormat simpleformat = new SimpleDateFormat("dd-MMMM-yyyy ");


                                                      binding.tvDate.setText(simpleformat.format(Calendar.getInstance().getTime()));



                                                             //Fetching DriverName

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
                                                    JourneyModel journeyModel = task.getResult().toObject(JourneyModel.class);
                                                    binding.tvStartTimeItemSchedule2.setText(journeyModel.getStart());
                                                    binding.tvArrivalsTimeItemSchedule2.setText(journeyModel.getEnd());
                                                    binding.tvArrivalsPlace2.setText(journeyModel.getOrganization());
                                                    binding.tvStartingPlace2.setText(journeyModel.getRegion());


                                                    SimpleDateFormat simpleformat = new SimpleDateFormat("dd-MMMM-yyyy ");


                                                    binding.tvDate2.setText(simpleformat.format(Calendar.getInstance().getTime()));

                                                    Toast.makeText(getActivity(), journeyModel.getDriver(), Toast.LENGTH_LONG).show();
                                                    //Fetching DriverName

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

                            }
                        } else {
                            Log.d("Benf_schedule", task.getException().getMessage());
                        }
                    }
                });



        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.btnAccept2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

       binding.btnExclude.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

        binding.btnExclude2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });



        return binding.getRoot();




}
}