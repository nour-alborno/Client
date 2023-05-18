package com.example.client.Model;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Benf_Schedule {


    String journeyIdGoing;
    String journeyIdReturn;

    public Benf_Schedule() {
    }

    public Benf_Schedule(String journeyIdGoing, String journeyIdReturn) {
        this.journeyIdGoing = journeyIdGoing;
        this.journeyIdReturn = journeyIdReturn;
    }

    public String getJourneyIdGoing() {
        return journeyIdGoing;
    }

    public void setJourneyIdGoing(String journeyIdGoing) {
        this.journeyIdGoing = journeyIdGoing;
    }

    public String getJourneyIdReturn() {
        return journeyIdReturn;
    }

    public void setJourneyIdReturn(String journeyIdReturn) {
        this.journeyIdReturn = journeyIdReturn;
    }
}
