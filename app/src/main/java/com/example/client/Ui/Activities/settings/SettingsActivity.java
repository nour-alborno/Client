package com.example.client.Ui.Activities.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.R;
import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    int textSize = 20;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    TextView tv_login, tv_address, tv_age, tv_arrivals_place, tv_arrivals_time_item_schedule, tv_arrivals_time_item_schedule2, tv_attendance_status, tv_time_notification, tv_date2, tv_city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvText.setTextSize(textSize);
        binding.tvFontSize.setText(binding.sbSize.getProgress()+"/"+binding.sbSize.getMax());
        binding.sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressNew = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                sp = getSharedPreferences("sp", MODE_PRIVATE);
                edit = sp.edit();

                textSize = textSize+(progress - progressNew);
                progressNew = progress;
                binding.tvText.setTextSize(textSize);
                binding.tvSettings.setTextSize(textSize);
                binding.tvSettings2.setTextSize(textSize);
                binding.tvFontSize.setTextSize(textSize);
                binding.tvSound.setTextSize(textSize);
                binding.btnSave.setTextSize(textSize);
                binding.rbtnArabic.setTextSize(textSize);
                binding.rbtnEnglish.setTextSize(textSize);
                binding.tvLanguage.setTextSize(textSize);

                tv_login = findViewById(R.id.tv_login);
                tv_address = findViewById(R.id.tv_address);
                tv_age = findViewById(R.id.tv_age);
                tv_arrivals_place = findViewById(R.id.tv_arrivals_place);
                tv_arrivals_time_item_schedule = findViewById(R.id.tv_arrivals_time_item_schedule);
                tv_arrivals_time_item_schedule2 = findViewById(R.id.tv_arrivals_time_item_schedule2);
                tv_attendance_status = findViewById(R.id.tv_attendance_status);
                tv_time_notification = findViewById(R.id.tv_time_notification);
                tv_date2 = findViewById(R.id.tv_date2);
                tv_city = findViewById(R.id.tv_city);
                findViewById(R.id.tv_code);
                findViewById(R.id.tv_phone);
                findViewById(R.id.tv_resend);
                findViewById(R.id.tv_description);
                findViewById(R.id.tv_organization);
                findViewById(R.id.tv_title);
                findViewById(R.id.tv_starting_place2);
                findViewById(R.id.tv_starting_place);
                findViewById(R.id.tv_gender);
                findViewById(R.id.tv_contactus);
                findViewById(R.id.tv_content);
                findViewById(R.id.tv_arrivals_place2);
                findViewById(R.id.tv_enterCode);
                findViewById(R.id.tv_hintCode);
                findViewById(R.id.tv_type_of_disability);
                findViewById(R.id.tv_date);
                findViewById(R.id.tv_description_notification);
                findViewById(R.id.tv_driver_name);
                findViewById(R.id.tv_driver_name2);
                findViewById(R.id.tv_edit_photo);
                findViewById(R.id.tv_start_time_item_schedule);
                findViewById(R.id.tv_start_time_item_schedule2);
                findViewById(R.id.tv_name);
                findViewById(R.id.tv_number);
                findViewById(R.id.tv_notification_title);

                tv_login.setTextSize(textSize);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.tvFontSize.setText(progressNew+"/"+binding.sbSize.getMax());
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.vibrateButtonClicked(getBaseContext());
            }
        });

    }
}