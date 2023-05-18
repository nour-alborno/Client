package com.example.client.Ui.Activities.Main;

import androidx.fragment.app.Fragment;

public interface MainView {
    void onSetFragment(Fragment fragment);

    void onSelectedNavIcon(Fragment fragment);
}
