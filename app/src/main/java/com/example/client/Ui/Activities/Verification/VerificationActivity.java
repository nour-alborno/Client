package com.example.client.Ui.Activities.Verification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;
import com.example.client.Ui.Activities.Main.MainActivity;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.databinding.ActivityVerificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerificationActivity extends AppCompatActivity implements VerificationView{
    ActivityVerificationBinding binding;
    String verificationId;
    private PhoneAuthProvider.ForceResendingToken token;

String newNumber,verificationIdEdit;

    SharedPreferences sp;
    SharedPreferences.Editor edit;

    public final String CLIENT_ID_KEY = "clientId";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        VerificationPresenter presenter = new VerificationPresenter(this);

            verificationId = getIntent().getStringExtra("verificationId");
            token = getIntent().getParcelableExtra("resendingToken");

        boolean fromlogin = getIntent().getBooleanExtra("fromWhere",false);
        newNumber = getIntent().getStringExtra("number");
        verificationIdEdit = getIntent().getStringExtra("verificationIdEdit");



        if (fromlogin){
            binding.btnLogin.setOnClickListener(view -> {
                setEnabledVisibility();
                Log.e("VerificationActivityLOG","Click");
                if (binding.pinView.getText().toString().trim().isEmpty()) {
                    binding.pinView.setError("Enter your phone number");
                    binding.pinView.setLineColor(getResources().getColor(R.color.baby_red));
                    AppUtility.showSnackbar(binding.getRoot(),"Enter your phone number");
                    Log.e("VerificationActivityLOG", "empty");
                    setEnabledVisibility();
                    return;
                }else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnLogin.setBackgroundColor(getResources().getColor(R.color.gray));
                    binding.btnLogin.setText(R.string.sending);
                    binding.pinView.setEnabled(false);
                    binding.btnLogin.setEnabled(false);
                }


                String code = binding.pinView.getText().toString();
                if (verificationId != null) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnLogin.setBackgroundColor(getResources().getColor(R.color.gray));

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(task -> {
                                //  Log.e("VerificationActivityLOG","   =====>  "+task.getException().getMessage().toString());
                                setEnabledVisibility();
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
                                } else {
                                    binding.pinView.setLineColor(getResources().getColor(R.color.baby_red));
                                    AppUtility.showSnackbar(binding.getRoot(),task.getException().getMessage());

                                }
                            });
                    //.addOnFailureListener(e -> Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        }else {

            Log.d("froWhere","inside: "+fromlogin);
            binding.btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setEnabledVisibility();
                    Log.e("VerificationActivityLOG", "Click");
                    if (TextUtils.isEmpty(binding.pinView.getText().toString())) {
                        binding.pinView.setError("Enter verification code");
                        binding.pinView.setLineColor(getResources().getColor(R.color.baby_red));
                        AppUtility.showSnackbar(binding.getRoot(), "Enter verification code");
                        setEnabledVisibility();
                        Log.e("VerificationActivityLOG", "empty");
                        return;
                    } else {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.btnLogin.setBackgroundColor(getResources().getColor(R.color.gray));
                        binding.btnLogin.setText(R.string.sending);
                        binding.pinView.setEnabled(false);
                        binding.btnLogin.setEnabled(false);


                        String code = binding.pinView.getText().toString();
                        if (verificationIdEdit != null) {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            binding.btnLogin.setBackgroundColor(getResources().getColor(R.color.gray));


                            Log.d("inside if", "inside");
                            Log.d("verificationIdEdit", getIntent().getStringExtra("verificationIdEdit"));
                            Log.d("code", binding.pinView.toString().trim());

                            presenter.updatePhoneNumber(getIntent().getStringExtra("verificationIdEdit"), binding.pinView.getText().toString(), sp.getString(CLIENT_ID_KEY, null), Integer.parseInt(newNumber));
                        }
                    }

                }
            });
        }

//        binding.tvResend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                {
//                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                    FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
//                    firebaseAuthSettings.setAppVerificationDisabledForTesting(false);
//
//                    PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(firebaseAuth);
//                    builder.setPhoneNumber("+972" + getIntent().getStringExtra("mobile"));
//                    builder.setTimeout(60L, TimeUnit.SECONDS);
//                    builder.setActivity(VerificationActivity.this);
//                    builder.setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                        @Override
//                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                            setEnabledVisibility();
//                            Log.d("VerificationActivityLOG", "onVerificationCompleted   :    failed");
//                        }
//
//                        @Override
//                        public void onVerificationFailed(@androidx.annotation.NonNull FirebaseException e) {
//                            setEnabledVisibility();
//                            Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.d("VerificationActivityLOG", "onVerificationFailed   :  " + e.getMessage());
//                        }
//
//                        @Override
//                        public void onCodeSent(@NonNull String newVerificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                            setEnabledVisibility();
//                            verificationId = newVerificationId;
//                            Toast.makeText(VerificationActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
//                            Log.d("VerificationActivityLOG", "onCodeSent   :  ");
//                        }
//                    });
//
//                    String code = binding.pinView.getText().toString();
//                    if (verificationId != null) {
//                        binding.progressBar.setVisibility(View.VISIBLE);
//                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
//                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
//                                .addOnCompleteListener(task -> {
//                                    Log.e("VerificationActivityLOG", "   =====>  " + task.getException().getMessage());
//                                    binding.progressBar.setVisibility(View.GONE);
//                                    //setEnabledVisibility();
//                                    if (task.isSuccessful()) {
//                                        Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//                                        Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .addOnFailureListener(e -> Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
//                    }
//                }
//            }
//        });







//        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String code = binding.pinView.getText().toString();
//
//                if (TextUtils.isEmpty(code)) {
//                    Toast.makeText(VerificationActivity.this, "Enter your code OTP", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                //  verifyCode(code);
//                if (verificationId != null) {
//                    binding.progressBar.setVisibility(View.VISIBLE);binding.btnLogin.setText(R.string.verifying);
//                    binding.btnLogin.setEnabled(false);
//                    binding.pinView.setEnabled(false);
//                    binding.tvResend.setEnabled(false);
//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//
//
//                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            setEnabledVisibility();
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d("VVVTAG", "signInWithCredential:success");
//                                Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
//                                //todo
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                //finish();
//                            } else {
//                                // Sign in failed, display a message and update the UI
//                                Log.d("VVVTAG", "signInWithCredential:failed" + task.getException().getMessage());
//                                Toast.makeText(VerificationActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//
//        binding.tvResend.setOnClickListener(view -> {
//
//            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//            FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
//            firebaseAuthSettings.setAppVerificationDisabledForTesting(false);
//
//            PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(firebaseAuth);
//            builder.setPhoneNumber("+970" +getIntent().getStringExtra("mobile"));
//            builder.setTimeout(60L, TimeUnit.SECONDS);
//            builder.setActivity(VerificationActivity.this);
//            builder.setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                @Override
//                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                    setEnabledVisibility();
//                    Log.d("VVVTAG", "onVerificationCompleted   :    failed");
//                }
//
//                @Override
//                public void onVerificationFailed(@NonNull FirebaseException e) {
//                    setEnabledVisibility();
//                    Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.d("VVVTAG", "onVerificationFailed   :  " + e.getMessage());
//                }
//
//                @Override
//                public void onCodeSent(@NonNull String newVerificationId, PhoneAuthProvider.@NonNull ForceResendingToken forceResendingToken) {
//                    setEnabledVisibility();
//                    verificationId = newVerificationId;
//                    Toast.makeText(VerificationActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
//                    Log.d("VVVTAG", "onCodeSent   :  ");
//                }
//            });
//            PhoneAuthOptions options = builder
//                    .build();
//
//            firebaseAuth.setLanguageCode("en"); // or any other language code
//            PhoneAuthProvider.verifyPhoneNumber(options);
//
//
//
//            if (binding.tvResend.isClickable()) {
//                binding.tvResend.setTextColor(Color.parseColor("#417F7A")); // set text color to green
//            } else {
//                binding.tvResend.setTextColor(Color.parseColor("#8B8B8B")); // set text color to gray
//            }
//
//
//
////                PhoneAuthProvider.getInstance().verifyPhoneNumber("+970" + getIntent().getStringExtra("mobile"), 60, TimeUnit.SECONDS, VvvvvActivity.this,
////                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
////
////                            @Override
////                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
////                                setEnabledVisibility();
////                                Log.d("VVVTAG", "onVerificationCompleted   :    failed");
////                            }
////
////                            @Override
////                            public void onVerificationFailed(@NonNull FirebaseException e) {
////                                setEnabledVisibility();
////                                Toast.makeText(VvvvvActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
////                                Log.d("VVVTAG", "onVerificationFailed   :  " + e.getMessage());
////                            }
////
////                            @Override
////                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
////                                // super.onCodeSent(verificationId, forceResendingToken);
////                                setEnabledVisibility();
////                                verificationId = newVerificationId;
////                                Toast.makeText(VvvvvActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
////                                Log.d("VVVTAG", "onCodeSent   :  ");
////
////                            }
////                        });
//
//        });

    }


    private void setEnabledVisibility(){
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setText(R.string.verify);
        binding.btnLogin.setEnabled(true);
        binding.pinView.setEnabled(true);
        binding.tvResend.setEnabled(true);
        binding.btnLogin.setBackgroundColor(getResources().getColor(R.color.sea_green));
    }

    @Override
    public void onChangingNumberSuccess() {
        AppUtility.vibrateButtonClicked(getBaseContext());
        setEnabledVisibility();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }

    @Override
    public void onChangingNumberFailure(Exception e) {
        setEnabledVisibility();

        AppUtility.showSnackbar(binding.getRoot(),e.getMessage());
        AppUtility.vibrateError(getBaseContext());
    }
}