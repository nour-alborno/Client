package com.example.client.Ui.Activities.about_us;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends AppCompatActivity {

    ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ActivityResultLauncher<String> arl =registerForActivityResult
                (new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result){
                            Toast.makeText(AboutUsActivity.this, "Call Permission granted", Toast.LENGTH_SHORT).show();
                        //    AppUtility.showSnackbar(binding.getRoot(),"Call Permission granted");
                        }else {
                            AppUtility.showSnackbar(binding.getRoot(),"Call Permission denied");
                        }
                    }
                });
        arl.launch(Manifest.permission.CALL_PHONE);

        binding.btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.vibrateButtonClicked(getBaseContext());
                Intent intent=new Intent(Intent.ACTION_CALL);
                String phone = "+970593887076";
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);


            }
        });


        binding.btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.vibrateButtonClicked(getBaseContext());
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                String mail =" info@interpal.org";
                intent.setData(Uri.parse("mailto:"+mail));
                startActivity(intent);
            }
        });


    }
}