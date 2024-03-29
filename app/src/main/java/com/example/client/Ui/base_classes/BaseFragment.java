package com.example.client.Ui.base_classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {


   public SharedPreferences sp;
    public SharedPreferences.Editor edit;
    public final String CLIENT_ID_KEY = "clientId";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.id, container, false);


        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        edit = sp.edit();




        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
