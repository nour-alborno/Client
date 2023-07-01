package com.example.client.Ui.Fragments.Profile;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.client.Model.Benefeciares;
import com.example.client.Ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfilePresenter extends BasePresenter {

    ProfileView view;

    public ProfilePresenter(ProfileView view) {
        this.view = view;
    }


    public void benfInfo(String benfId) {
        firestore.collection("Beneficiaries").document(benfId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d("Beneficiaries", task.getResult().toString());
                        if (task.isSuccessful()) {

                            Benefeciares benefeciares = task.getResult().toObject(Benefeciares.class);
                            view.onbenfInfoSuccess(benefeciares);


                        } else {

                            view.onbenfInfoFailure(task.getException());


                        }

                    }
                });

    }


    public void gettingProfileImage(String benfId) {
        firestore.collection("Beneficiaries").document(benfId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            String imgUrl = (String) task.getResult().get("ImgUrl");
                            view.onGettingImgeSuccess(imgUrl);
                        } else {
                            view.onGettingImgFailure(task.getException());
                        }

                    }
                });
    }
}
