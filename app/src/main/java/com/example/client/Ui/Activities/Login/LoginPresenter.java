package com.example.client.Ui.Activities.Login;


import com.example.client.Model.DriverProfile;
import com.google.android.gms.maps.model.LatLng;

public class LoginPresenter {

    DriverProfile view;

    public LoginPresenter(DriverProfile view) {
        this.view = view;
    }


    public DriverProfile getMobileFromModel(){
        //get data from database
        return new DriverProfile("Taghreed",593887076,"Alzaytoon",1,"sunday",new LatLng(31.4860128,34.4471761));
    }

    public void getMobile(){
    }
}
