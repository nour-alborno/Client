package com.example.client.Ui.base_classes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public SharedPreferences sp;
   public SharedPreferences.Editor edit;
    public final String CLIENT_ID_KEY = "clientId";

    public String clientId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();






    }
}
