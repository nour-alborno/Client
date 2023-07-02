package com.example.client.Ui.Activities.Login;

import com.example.client.Model.CliantsNumbers;

public interface LoginView {
    void onFail(Exception exception);
    void isDriver(CliantsNumbers num);
    void numberNotFound();
}
