package com.example.client.Ui.Activities.EditProfile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.client.Ui.AppUtility.ProgressDialogUtility;
import com.example.client.Ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;

public class EditProfilePresenter extends BasePresenter {

    EditProfileView view;

    public EditProfilePresenter(EditProfileView view) {
        this.view = view;
    }


    void uploadImage(Uri profile_img_uri, Context context, String clientId){

        //Setting the progress
//        ProgressDialog progressDialog= new ProgressDialog(EditProfileActivity.this);
//        progressDialog.setTitle("Uploading...");
//        progressDialog.show();

        ProgressDialog myDialog= ProgressDialogUtility.showProgressBar(context.getApplicationContext(),"Uploading...");


        StorageReference storageReference = firebaseStorage.getReference("BeneficiariesImages/" + clientId +"/"+profile_img_uri.getLastPathSegment());
        StorageTask<UploadTask.TaskSnapshot> uploadTask = storageReference.putFile(profile_img_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully
                        // Dismiss dialog
                        myDialog.dismiss();
                    //    progressDialog.dismiss();
                        Log.d("success","Success");
                      //  Toast.makeText(getBaseContext(),"Image Uploaded!!", Toast.LENGTH_SHORT).show();



                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                              Uri  imgUrl = task.getResult();
                                //storeImgInProfile();
                                Log.d("bb",imgUrl.toString());
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();

                        // Error, Image not uploaded
                        myDialog.dismiss();
                     //   progressDialog.dismiss();
                        Log.d("Failure", e.getMessage());
                      //  Toast.makeText(getBaseContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        myDialog.setMessage("Uploaded " + (int)progress + "%");
                        myDialog.setProgress((int) progress);
//                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
//                        progressDialog.setProgress((int) progress);
                    }
                });

    }


    public void gettingProfileImage(String driverId){
        firestore.collection("Beneficiaries").document(driverId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            String imgUrl  = (String) task.getResult().get("ImgUrl");
                            view.onGettingImgeSuccess(imgUrl);
                        }else {
                            view.onGettingImgFailure(task.getException());
                        }

                    }
                });
    }

    public void updateNumber(String newNumber, Activity activity) {
        PhoneAuthCredential credentialCompleted ;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+970" + newNumber,
                60, TimeUnit.SECONDS,
                activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.e("LoginActivityLOG", "done");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d("verification",e.getMessage());
                        //    setEnabledVisibility();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);

                        view.onSendCode( verificationId, newNumber);


                    }
                }
        );



    }
}
