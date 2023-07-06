package com.example.client.adpters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.databinding.ItemNotificationBinding;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotVH> {

    public NotificationAdapter() {
    }

    @NonNull
    @Override
    public NotVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new NotVH(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull NotVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class NotVH extends RecyclerView.ViewHolder{
        public NotVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
