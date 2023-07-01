package com.example.client.Ui.Activities.History;

import com.example.client.Model.ArichivedJourney;

import java.util.ArrayList;

public interface HistoryView {
    void onGettingArchivedJourneysSuccess(ArrayList<ArichivedJourney> journeys);
    void onGettingArchivedJourneysFailure(Exception e);
}
