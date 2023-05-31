package com.example.client.Ui.Activities.EditProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.client.Model.Benefeciares;
import com.example.client.R;
import com.example.client.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Benefeciares benf_profile = getIntent().getParcelableExtra("userInfo");

        binding.etMobile.setText(benf_profile.getMobile());
        binding.tvName.setText(benf_profile.getName());
        binding.tvAddress.setText(benf_profile.getAddress());
        binding.tvAge.setText(benf_profile.getAge());
        binding.tvCity.setText(benf_profile.getRegion());
        binding.tvGender.setText(benf_profile.getGender());
        binding.tvOrganization.setText(benf_profile.getOrganization());
        binding.tvTypeOfDisability.setText(benf_profile.getDisabilityType());


    }
}