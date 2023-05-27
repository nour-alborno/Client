package com.example.client.Ui.Fragments.Home;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.client.Model.DriverProfile;
import com.example.client.databinding.ItemContactDialogBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class CustomDialogFragment extends DialogFragment {

    FirebaseFirestore firestore;
    DriverProfile driverProfile;
    public FirebaseStorage firebaseStorage;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // Inflate the layout for the dialog
            ItemContactDialogBinding binding = ItemContactDialogBinding.inflate(inflater, container, false);


            firestore = FirebaseFirestore.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();


            firestore.collection("Driver").document("1").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Log.d("CustomDialogFragmentTAG",task.getResult().toString());
                            if (task.isSuccessful()){

                                driverProfile = task.getResult().toObject(DriverProfile.class);
                                Log.d("CustomDialogFragmentTAG",task.getResult().toString());
                                binding.tvName.setText(driverProfile.getName());
                                binding.tvPhone.setText("+970 ".concat(String.valueOf(driverProfile.getMobile())));
                               // binding.imgProfile.setImageURI(driverProfile.getImage);

                            } else {

                                Log.d("CustomDialogFragmentTAG", task.getException().getMessage());

                            }

                        }
                    });


            ActivityResultLauncher<String> arl =registerForActivityResult
                    (new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            if (result){
                                Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            arl.launch(Manifest.permission.CALL_PHONE);


            binding.imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent=new Intent(Intent.ACTION_DIAL);
//                    String num =binding.tvPhone.getText().toString();
//                    intent.setData(Uri.parse("tel:"+num));
//                    startActivity(intent);
//                    dismiss();



                    Intent intent=new Intent(Intent.ACTION_CALL);
                    String num =binding.tvPhone.getText().toString();
                    intent.setData(Uri.parse("tel:"+num));
                    startActivity(intent);


                }
            });
            return binding.getRoot();
        }

}
