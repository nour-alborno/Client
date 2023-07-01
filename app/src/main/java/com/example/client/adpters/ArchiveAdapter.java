package com.example.client.adpters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Model.ArichivedJourney;
import com.example.client.databinding.ItemHistoryBinding;


import java.util.ArrayList;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.AVH> {

ArrayList<ArichivedJourney> journeys;

    public ArchiveAdapter(ArrayList<ArichivedJourney> journeys) {
        this.journeys = journeys;
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
    //    holder.status.setText(j.);

    }

    @Override
    public int getItemCount() {
        return journeys != null? journeys.size():0;
    }

    class AVH extends RecyclerView.ViewHolder {

        TextView start, end, to,from,status,date;

        public AVH(@NonNull ItemHistoryBinding binding) {
            super(binding.getRoot());

            start = binding.tvStartTimeItemSchedule;
            end =binding.tvArrivalsTimeItemSchedule;
            to=binding.tvArrivalsPlace;
            from=binding.tvStartingPlace;
            status=binding.tvAttendanceStatus;
            date=binding.tvDate;
        }
    }
}
