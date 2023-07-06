package com.example.client.adpters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.client.Model.ArichivedJourney;
import com.example.client.Model.DriverProfile;
import com.example.client.databinding.ItemHistoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.AVH> {

ArrayList<ArichivedJourney> journeys;
FirebaseFirestore firestore;
String driverName,driverImg;
Context context;

    public ArchiveAdapter(ArrayList<ArichivedJourney> journeys, Context context) {
        this.journeys = journeys;
        this.context = context;
        firestore=FirebaseFirestore.getInstance();
    }

    public ArchiveAdapter() {
    }

    @NonNull
    @Override
    public AVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AVH holder, int position) {

        if (journeys == null){
            return;
        }




        ArichivedJourney j = journeys.get(position);

        holder.date.setText(j.getDate());
        holder.to.setText(j.getOrganization());
        holder.from.setText(j.getRegion());
        holder.end.setText(j.getEnd());
        holder.start.setText(j.getStart());

        firestore.collection("Driver").document(j.getDriver())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){
                            DriverProfile driverProfile = task.getResult().toObject(DriverProfile.class);
                            driverImg = driverProfile.getImgUrl();
                            driverName = driverProfile.getName();



                            Glide.with(context).load(driverProfile.getImgUrl())
                                    .into(holder.driverImg);

                            holder.driverName.setText(driverProfile.getName());
                            Log.d("suceess",driverProfile.toString());
                        }else {
                            Log.d("driverInfoFailed",task.getException().getMessage());
                        }



                    }
                });

//        gettingDriverInfo(j.getDriver());
//        if (driverImg != null && driverName != null){
//            holder.driverImg.setImageURI(Uri.parse(driverImg));
//            holder.driverName.setText(driverName);
//        }
    //    holder.status.setText(j.);

    }

    @Override
    public int getItemCount() {
        return journeys != null? journeys.size():0;
    }

    class AVH extends RecyclerView.ViewHolder {

        TextView start, end, to,from,status,date,driverName;

        ImageView driverImg;

        public AVH(@NonNull ItemHistoryBinding binding) {
            super(binding.getRoot());

            start = binding.tvStartTimeItemSchedule;
            end =binding.tvArrivalsTimeItemSchedule;
            to=binding.tvArrivalsPlace;
            from=binding.tvStartingPlace;
            status=binding.tvAttendanceStatus;
            date=binding.tvDate;
            driverImg = binding.imgProfile;
            driverName=binding.tvDriverName;
        }
    }

    void gettingDriverInfo(String driverId){



    }

}
