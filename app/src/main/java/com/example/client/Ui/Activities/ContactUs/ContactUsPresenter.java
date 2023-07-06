package com.example.client.Ui.Activities.ContactUs;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.client.Model.ContactUs;

import com.example.client.Ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ContactUsPresenter extends BasePresenter {
    ContactUsView view;

    public ContactUsPresenter(ContactUsView view) {
        this.view = view;
    }

    public void sendingMessage(ContactUs contactUs){

        ArrayList<ContactUs> contactUsArrayList = new ArrayList<>();
        contactUsArrayList.add(contactUs);
        DatabaseReference   reference = db.getReference("ClientMessage");
        reference.child("Messages").push().setValue(contactUs)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            view.onSendingMessageSuccess();
                            Log.d("contactUs","succesfull");
                        } else {
                            view.onSendingMessageFailure(task.getException());
                            Log.d("contactUs",task.getException().getMessage());
                        }
                    }
                });;
    }
}
