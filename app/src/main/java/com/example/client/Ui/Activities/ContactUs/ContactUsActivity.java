package com.example.client.Ui.Activities.ContactUs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.client.Model.ContactUs;
import com.example.client.R;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.databinding.ActivityContactUsBinding;

public class ContactUsActivity extends AppCompatActivity implements ContactUsView{

    ActivityContactUsBinding binding;

    public final String CLIENT_ID_KEY = "clientId";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ContactUsPresenter presenter = new ContactUsPresenter(this);
        sp =getSharedPreferences("sp", Context.MODE_PRIVATE);

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactUs contactUs = new ContactUs();
                contactUs.setDate(AppUtility.getDateTime());
                contactUs.setMessage(binding.etMessage.getText().toString());
                contactUs.setTitle(binding.etSubject.getText().toString());
                contactUs.setClientId(sp.getString(CLIENT_ID_KEY,null));

                presenter.sendingMessage(contactUs);
            }
        });

    }

    @Override
    public void onSendingMessageSuccess() {

        Toast toast = new Toast(ContactUsActivity.this);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100); // Position at the top with a vertical offset
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText("Your Message Has Been Sent. \n Thank you for contacting us!");

        // Show the Toast
        toast.show();

        binding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1200);
    }
    }
