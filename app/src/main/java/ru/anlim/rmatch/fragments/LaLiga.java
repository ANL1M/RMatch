package ru.anlim.rmatch.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.anlim.rmatch.MainActivity;
import ru.anlim.rmatch.R;

public class LaLiga extends MainActivity.PlaceholderFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_la_liga, container, false);
    }

}
