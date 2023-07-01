package com.example.client.Ui.Activities.History;

import androidx.annotation.NonNull;

import com.example.client.Model.ArichivedJourney;
import com.example.client.Ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryPresenter extends BasePresenter {
    HistoryView view;

    public HistoryPresenter(HistoryView view) {
        this.view = view;
    }


    void gettingArchivedJourneys(String driverId){

        firestore.collection("Journey_Archive").document(driverId)
                .collection("Journeys").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){


                            ArrayList<ArichivedJourney> journeys = (ArrayList<ArichivedJourney>) task.getResult().toObjects(ArichivedJourney.class);
                            view.onGettingArchivedJourneysSuccess(journeys);
                        }else {
                            view.onGettingArchivedJourneysFailure(task.getException());
                        }
                    }
                });

    }
}
