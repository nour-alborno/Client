package com.example.client.Ui.Activities.EditProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.client.Model.Benefeciares;
import com.example.client.R;
import com.example.client.Ui.Activities.Verification.VerificationActivity;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.Ui.AppUtility.ProgressDialogUtility;
import com.example.client.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements EditProfileView{

    ActivityEditProfileBinding binding;
    EditProfilePresenter editProfilePresenter;
    Benefeciares benf_profile;

    FirebaseStorage firebaseStorage;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         editProfilePresenter = new EditProfilePresenter(this);

         firebaseStorage=FirebaseStorage.getInstance();
         firestore=FirebaseFirestore.getInstance();

        benf_profile = getIntent().getParcelableExtra("userInfo");

        binding.etMobile.setText(String.valueOf(benf_profile.getMobile()));
        binding.tvName.setText(benf_profile.getName());
        binding.tvAddress.setText(benf_profile.getAddress());
        binding.tvAge.setText(benf_profile.getAge());
        binding.tvCity.setText(benf_profile.getRegion());
        binding.tvGender.setText(benf_profile.getGender());
        binding.tvOrganization.setText(benf_profile.getOrganization());
        binding.tvTypeOfDisability.setText(benf_profile.getDisabilityType());


        editProfilePresenter.gettingProfileImage(benf_profile.getDocumentId());




        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inetent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(inetent, 10);
            }
        });

        binding.etMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String newNumber = binding.etMobile.getText().toString().trim();

                    View view = getCurrentFocus();

                    if (!newNumber.isEmpty()){
                        AppUtility.vibrateButtonClicked(getBaseContext());


                        AppUtility.hideSoftKeyboard(getBaseContext(),view);

                        editProfilePresenter.updateNumber(newNumber,EditProfileActivity.this);
                      //  updateNumber(newNumber);
                        return true;
                    } else {

                        AppUtility.hideSoftKeyboard(getBaseContext(),view);

                        AppUtility.vibrateError(getBaseContext());
                        AppUtility.showSnackbar(binding.getRoot(),"Enter the new number");
                    }
                }

                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri img_uri = data.getData();
            binding.imgProfile.setImageURI(img_uri);

            if (img_uri != null) uploadImage(img_uri,benf_profile.getDocumentId());

        }
    }


    void uploadImage(Uri profile_img_uri, String clientId){

        //Setting the progress
        ProgressDialog progressDialog= new ProgressDialog(EditProfileActivity.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();




        StorageReference storageReference = firebaseStorage.getReference("BeneficiariesImages/" + clientId +"/"+profile_img_uri.getLastPathSegment());
        StorageTask<UploadTask.TaskSnapshot> uploadTask = storageReference.putFile(profile_img_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully
                        // Dismiss dialog

                            progressDialog.dismiss();
                        Log.d("success","Success");
                          Toast.makeText(getBaseContext(),"Image Uploaded!!", Toast.LENGTH_SHORT).show();



                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                Uri  imgUrl = task.getResult();
                                storeImgInProfile(imgUrl,clientId);
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

                          progressDialog.dismiss();
                        Log.d("Failure", e.getMessage());
                          Toast.makeText(getBaseContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());

                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        progressDialog.setProgress((int) progress);
                    }
                });

    }

    void storeImgInProfile(Uri imgUrl, String clientId){
        Map<String , Object> setImg = new HashMap<>();
        setImg.put("ImgUrl",  imgUrl.toString());

        firestore.collection("Beneficiaries").document(clientId).update(setImg)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d(getClass().getSimpleName(), "Successful");
                        } else {
                            Log.d(getClass().getSimpleName(), task.getException().getMessage());         }
                    }
                });
    }

    @Override
    public void onGettingImgeSuccess(String img) {
        Glide.with(getBaseContext()).load(img)
                .into(binding.imgProfile);

        Log.d("Success",img);
    }

    @Override
    public void onGettingImgFailure(Exception e) {
        binding.imgProfile.setImageResource(R.drawable.profile_avtar);
        Log.d("FailureImg",e.getMessage());
    }

    @Override
    public void onSendCode(String verificationId, String newNumber) {
        Log.d("verificationIdEdit",verificationId+"  "+"from edit");
        Intent intent = new Intent(EditProfileActivity.this, VerificationActivity.class);
        intent.putExtra("verificationIdEdit", verificationId);
        intent.putExtra("number", newNumber);
        intent.putExtra("fromWhere",false);
        startActivity(intent);
        finish();
    }
}
