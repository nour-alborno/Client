package com.example.client.Ui.Activities.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.Ui.AppUtility.AppUtility;
import com.example.client.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    int textSize = 20;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

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