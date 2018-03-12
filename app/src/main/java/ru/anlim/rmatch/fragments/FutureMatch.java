package ru.anlim.rmatch.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.anlim.rmatch.MainActivity;
import ru.anlim.rmatch.R;

public class FutureMatch extends MainActivity.PlaceholderFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_future_match, container, false);
    }
}
