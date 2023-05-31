package com.example.client.Ui.Activities.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.Model.CliantsNumbers;
import com.example.client.R;
import com.example.client.Ui.Activities.Main.MainActivity;
import com.example.client.Ui.Activities.Verification.VerificationActivity;
import com.example.client.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;
public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseFirestore firestore;
    CliantsNumbers cliantsNumbers;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    public final String CLIENT_ID_KEY = "clientId";
    public final String CLIENT_NUMBER_KEY = "driverNumber";


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        firestore = FirebaseFirestore.getInstance();



        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobile = binding.etMobile.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    binding.etMobile.setError("Enter your phone number");
                    setEnabledVisibility();
                    Toast.makeText(getApplicationContext(), "Enter your phone", Toast.LENGTH_SHORT).show();
                    return;
                }

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnLogin.setText(R.string.sending);
                binding.btnLogin.setEnabled(false);


                firestore.collection("Benf_Numbers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {



                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot document : task.getResult()){
                                CliantsNumbers num = document.toObject(CliantsNumbers.class);

                                if (binding.etMobile.getText().toString().equals(String.valueOf(num.getMobile()))){
                                    Log.d("LoginActivityLOG",String.valueOf(num.getMobile()));
                                    num.getId();
                                    edit.putString(CLIENT_ID_KEY,num.getId());
                                    edit.putString(CLIENT_NUMBER_KEY,String.valueOf(num.getMobile()));
                                    edit.commit();
                                    sendCodeVerification();
                                   // binding.etMobile.setText("");

                                }else {
                                    Log.d("LoginActivityLOG","Does not exist");
                                    setEnabledVisibility();
                                }
                            }


                        } else {
                            Log.d("LoginActivityLOG",task.getException().getMessage());
                        }
                    }
                });
//                if (binding.etMobile.getText().toString() != sp.getString(DRIVER_NUMBER_KEY,"not found")){
//                    Toast.makeText(LoginActivity.this, "your not Allow", Toast.LENGTH_SHORT).show();
//                }

            }
        });

    }

    private void sendCodeVerification() {
        String phone = binding.etMobile.getText().toString().trim();
        Log.e("LoginActivityLOG", phone);



        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+970" + phone,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.e("LoginActivityLOG", "done");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivityLOG", e.toString());
                        binding.etMobile.setText("");
                        setEnabledVisibility();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);
                        binding.progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("resendingToken", token);
                        setEnabledVisibility();
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setEnabledVisibility();
    }
    private void setEnabledVisibility(){
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setText(R.string.send);
        binding.etMobile.setEnabled(true);
        binding.btnLogin.setEnabled(true);
    }
}





