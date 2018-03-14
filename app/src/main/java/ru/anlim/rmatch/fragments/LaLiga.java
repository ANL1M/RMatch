package ru.anlim.rmatch.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import ru.anlim.rmatch.MainActivity;
import ru.anlim.rmatch.R;
import ru.anlim.rmatch.logic.DBHelper;

public class LaLiga extends MainActivity.PlaceholderFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_future_match, container, false);

        DBHelper dbHelper = new DBHelper(getActivity());
        Cursor cursor = dbHelper.dbReadLiga();
        ListView lvLiga = rootView.findViewById(R.id.lvLiga);

        return rootView;
    }

}
