package com.example.client.Ui.Activities.History;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.client.Model.ArichivedJourney;
import com.example.client.R;
import com.example.client.adpters.ArchiveAdapter;
import com.example.client.databinding.ActivityHistoryBinding;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements HistoryView {

    ActivityHistoryBinding binding;
    SharedPreferences sp;
    public final String CLIENT_ID_KEY = "clientId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HistoryPresenter presenter = new HistoryPresenter(this);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);



        presenter.gettingArchivedJourneys(  sp.getString(CLIENT_ID_KEY,null));



    }

    @Override
    public void onGettingArchivedJourneysSuccess(ArrayList<ArichivedJourney> journeys) {

        if (getContext() == null) return;

        binding.rvArchive.setAdapter(new ArchiveAdapter(journeys,getBaseContext()));
        binding.rvArchive.setLayoutManager(new LinearLayoutManager(HistoryActivity.this,LinearLayoutManager.VERTICAL,false));
    }




    @Override
    public void onGettingArchivedJourneysFailure(Exception e) {

        Log.d("failure",e.getMessage());
    }
}