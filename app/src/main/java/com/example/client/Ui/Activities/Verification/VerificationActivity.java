package com.example.client.Ui.Activities.Verification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;
import com.example.client.Ui.Activities.Login.LoginActivity;
import com.example.client.Ui.Activities.Main.MainActivity;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.databinding.ActivityVerificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity implements VerificationView{
    ActivityVerificationBinding binding;
    String verificationId;
    private PhoneAuthProvider.ForceResendingToken token;
    private String storedVerificationId;

    String phone;
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

        boolean fromlogin = getIntent().getBooleanExtra("fromWhere", false);
        newNumber = getIntent().getStringExtra("number");
        verificationIdEdit = getIntent().getStringExtra("verificationIdEdit");


        if (fromlogin) {
            binding.tvHintCode.setText(getString(R.string.secret_code) + " +970 " + getIntent().getStringExtra("phone"));
            binding.btnLogin.setOnClickListener(view -> {
                setEnabledVisibility();
                Log.e("VerificationActivityLOG", "Click");
                if (binding.pinView.getText().toString().trim().isEmpty()) {
                    binding.pinView.setError("Enter your phone number");
                    binding.pinView.setLineColor(getResources().getColor(R.color.baby_red));
                    AppUtility.showSnackbar(binding.getRoot(), "Enter your phone number");
                    AppUtility.vibrateButtonClicked(getBaseContext());
                    Log.e("VerificationActivityLOG", "empty");
                    setEnabledVisibility();
                    return;
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnLogin.setBackgroundColor(getResources().getColor(R.color.gray));
                    binding.btnLogin.setTextColor(getResources().getColor(R.color.sea_green));
                    binding.btnLogin.setText(R.string.sending);
                    binding.pinView.setEnabled(false);
                    binding.btnLogin.setEnabled(false);
                }


                String code = binding.pinView.getText().toString();
                if (verificationId != null) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnLogin.setBackgroundColor(getResources().getColor(R.color.gray));
                    binding.btnLogin.setTextColor(getResources().getColor(R.color.sea_green));

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
                                    AppUtility.showSnackbar(binding.getRoot(), task.getException().getMessage());

                                }
                            });
                    //.addOnFailureListener(e -> Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        } else {
            binding.tvHintCode.setText(getString(R.string.secret_code) + " +970 " + getIntent().getStringExtra("number"));
            Log.d("froWhere", "inside: " + fromlogin);
            binding.btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setEnabledVisibility();
                    Log.e("VerificationActivityLOG", "Click");
                    if (TextUtils.isEmpty(binding.pinView.getText().toString())) {
                        binding.pinView.setLineColor(getResources().getColor(R.color.baby_red));
                        AppUtility.showSnackbar(binding.getRoot(), "Enter verification code");
                        setEnabledVisibility();
                        Log.e("VerificationActivityLOG", "empty");
                        return;
                    } else {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.btnLogin.setBackgroundColor(getResources().getColor(R.color.gray));
                        binding.btnLogin.setTextColor(getResources().getColor(R.color.sea_green));
                        binding.btnLogin.setText(R.string.sending);
                        binding.pinView.setEnabled(false);
                        binding.btnLogin.setEnabled(false);


                        String code = binding.pinView.getText().toString();
                        if (verificationIdEdit != null) {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            binding.btnLogin.setBackgroundColor(getResources().getColor(R.color.gray));
                            binding.btnLogin.setTextColor(getResources().getColor(R.color.sea_green));


                            Log.d("inside if", "inside");
                            Log.d("verificationIdEdit", getIntent().getStringExtra("verificationIdEdit"));
                            Log.d("code", binding.pinView.toString().trim());

                            presenter.updatePhoneNumber(getIntent().getStringExtra("verificationIdEdit"), binding.pinView.getText().toString(), sp.getString(CLIENT_ID_KEY, null), Integer.parseInt(newNumber));
                        }
                    }

                }
            });
        }

        binding.tvResend.setVisibility(View.VISIBLE);

        binding.tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.tvResend.setTextColor(getResources().getColor(R.color.dark_gray));
                binding.tvResend.setEnabled(false);
                resendVerificationCode();
                AppUtility.vibrateButtonClicked(getBaseContext());
            }
        });


    }

    private void setEnabledVisibility(){
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setText(R.string.verify);
        binding.btnLogin.setEnabled(true);
        binding.pinView.setEnabled(true);
        binding.tvResend.setEnabled(true);
        binding.btnLogin.setTextColor(getResources().getColor(R.color.white));
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

    private void resendVerificationCode() {
        if (token != null) {

            Log.d("teeeeat1","+970" + LoginActivity.mobile);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+970" + LoginActivity.mobile,
                    60,
                    TimeUnit.SECONDS,
                    VerificationActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential credential) {
                            // This method will be called when verification is completed automatically
                            signInWithPhoneAuthCredential(credential);
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            Toast.makeText(VerificationActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken resendingToken) {
                            storedVerificationId = verificationId;
                            token = resendingToken;
                            startCountdownTimer();
                            Toast.makeText(VerificationActivity.this, "Verification code sent", Toast.LENGTH_SHORT).show();
                        }
                    },
                    token
            );
        } else {
            Toast.makeText(VerificationActivity.this, "Resending not supported", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCountdownTimer() {
        binding.tvResend.setEnabled(false);
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvResend.setText("Resend in " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                binding.tvResend.setEnabled(true);
                binding.tvResend.setText("Resend");
            }
        }.start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerificationActivity.this, "Verification successful", Toast.LENGTH_SHORT).show();
                            AuthResult authResult = task.getResult();
                            // Perform additional actions if needed
                        } else {
                            Toast.makeText(VerificationActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}