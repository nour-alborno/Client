package com.example.client.Ui.Activities.ContactUs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.client.Model.ContactUs;
import com.example.client.R;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.Ui.base_classes.BaseActivity;
import com.example.client.databinding.ActivityContactUsBinding;

public class ContactUsActivity extends BaseActivity implements ContactUsView{

    ActivityContactUsBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ContactUsPresenter presenter = new ContactUsPresenter(this);


        Log.d("Test", sp.getString(CLIENT_ID_KEY,null) );
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.etMessage.getText().toString().isEmpty()){
                    AppUtility.vibrateError(getBaseContext());
                    binding.etMessage.setError("Fill the field");
                } else if (binding.etSubject.getText().toString().isEmpty()) {
                    AppUtility.vibrateError(getBaseContext());
                    binding.etSubject.setError("Fill the field");
                } else {
                    ContactUs contactUs = new ContactUs();
                    contactUs.setDate(AppUtility.getDateTime());
                    contactUs.setMessage(binding.etMessage.getText().toString());
                    contactUs.setTitle(binding.etSubject.getText().toString());
                    contactUs.setClientId(sp.getString(CLIENT_ID_KEY,null));

                    presenter.sendingMessage(contactUs);
                }


            }
        });

    }

    @Override
    public void onSendingMessageSuccess() {

        AppUtility.vibrateButtonClicked(getApplicationContext());
        AppUtility.showSnackbar(binding.getRoot(),"We've received your message. \n Thank you for contacting us!");
        onBackPressed();

    }

    @Override
    public void onSendingMessageFailure(Exception e) {
        AppUtility.vibrateError(getBaseContext());
        AppUtility.showSnackbar(binding.getRoot(),"It appears there is a problem. Please try later");
    }
}
