package ru.anlim.rmatch.fragments;


import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ru.anlim.rmatch.MainActivity;
import ru.anlim.rmatch.R;
import ru.anlim.rmatch.logic.DBHelper;
import ru.anlim.rmatch.logic.PicaccoHelper;

public class LaLiga extends MainActivity.PlaceholderFragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_la_liga, container, false);

        //Инициализация сущностей
        DBHelper dbHelper = new DBHelper(getActivity());
        Cursor cursor = dbHelper.dbReadLiga();
        ListView lvLiga = rootView.findViewById(R.id.lvLiga);

        //Метод наполнения экрана данными
        setResult(lvLiga, cursor);

        return rootView;
    }

    public void setResult(ListView lvLiga, Cursor cursor){

        final PicaccoHelper PicaccoHelper = new PicaccoHelper();
        lvLiga.setAdapter(new CursorAdapter(getActivity(), cursor, 1) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return LayoutInflater.from(getActivity()).inflate(R.layout.item, viewGroup, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                TextView tvNumber   = view.findViewById(R.id.tvNumber);
                TextView tvTeam     = view.findViewById(R.id.tvTeam);
                TextView tvGames    = view.findViewById(R.id.tvGames);
                TextView tvWin      = view.findViewById(R.id.tvWin);
                TextView tvDraw     = view.findViewById(R.id.tvDraw);
                TextView tvLose     = view.findViewById(R.id.tvLose);
                TextView tvDiff     = view.findViewById(R.id.tvDiff);
                TextView tvPoints   = view.findViewById(R.id.tvPoints);
                ImageView ivTeam    = view.findViewById(R.id.imTeam);

                tvNumber.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("_id"))));
                tvTeam  .setText(cursor.getString(cursor.getColumnIndexOrThrow("Team")));
                tvGames .setText(cursor.getString(cursor.getColumnIndexOrThrow("Games")));
                tvWin   .setText(cursor.getString(cursor.getColumnIndexOrThrow("Wins")));
                tvDraw  .setText(cursor.getString(cursor.getColumnIndexOrThrow("Draw")));
                tvLose  .setText(cursor.getString(cursor.getColumnIndexOrThrow("Lose")));
                tvDiff  .setText(cursor.getString(cursor.getColumnIndexOrThrow("Diff")));
                tvPoints.setText(cursor.getString(cursor.getColumnIndexOrThrow("Points")));

                PicaccoHelper.LoadPic(cursor.getString(cursor.getColumnIndexOrThrow("ImageURL")), ivTeam);
            }
        });
    }
}
