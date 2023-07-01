package com.example.client.Ui.Activities.EditProfile;

public interface EditProfileView {

    void onGettingImgeSuccess(String img);
    void onGettingImgFailure(Exception e);

    void onSendCode(String verificationId, String newNumber);
}
