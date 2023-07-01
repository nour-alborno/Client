package com.example.client.Ui.Fragments.Profile;

import com.example.client.Model.Benefeciares;

public interface ProfileView  {

    void onbenfInfoSuccess(Benefeciares benefeciares);
    void onbenfInfoFailure(Exception e);



    void onGettingImgeSuccess(String img);
    void onGettingImgFailure(Exception e);
}
