package com.example.client.Ui.Activities.Main;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.example.client.R;
import com.example.client.Ui.Fragments.Attendance.AttendanceFragment;
import com.example.client.Ui.Fragments.Home.HomeFragment;
import com.example.client.Ui.Fragments.Profile.ProfileFragment;
import com.example.client.Ui.Fragments.notification.NotifiacationFragment;

public class MainPresenter {

    MainView view;


    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void AddingFrag(Fragment fragment){
        view.onSetFragment(fragment);

    }


    public void SelectingNavIcon(MenuItem item){
        Fragment fragment = null ;

        switch (item.getItemId()){

            case R.id.page_home:
               fragment = new HomeFragment();
                break;


            case R.id.page_attendance:
                fragment = new AttendanceFragment(); // ندى
                break;

            case R.id.page_notification:
                fragment = new NotifiacationFragment();
                break;

            case R.id.page_profile:
                fragment = new ProfileFragment();
                break;
        }

        view.onSelectedNavIcon(fragment);

    }



}
