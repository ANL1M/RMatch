package ru.anlim.rmatch.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import ru.anlim.rmatch.MainActivity;
import ru.anlim.rmatch.R;
import ru.anlim.rmatch.logic.DBHelper;

public class LaLiga extends MainActivity.PlaceholderFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_la_liga, container, false);

        DBHelper dbHelper = new DBHelper(getActivity());
        Cursor cursor = dbHelper.dbReadLiga();

        String s[] = new String[]{
                dbHelper.KEY_ID,
                dbHelper.Team,
                dbHelper.Games,
                dbHelper.Points
        };
        int i[] = new int[]{
                R.id.tvNumber,
                R.id.tvTeam,
                R.id.tvGames,
                R.id.tvPoins
        };

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item, cursor, s, i, 1);
        ListView lvLiga = rootView.findViewById(R.id.lvLiga);
        lvLiga.setAdapter(simpleCursorAdapter);

        return rootView;
    }
}
