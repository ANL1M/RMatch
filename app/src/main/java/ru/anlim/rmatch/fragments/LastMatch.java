package ru.anlim.rmatch.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import ru.anlim.rmatch.MainActivity;
import ru.anlim.rmatch.R;
import ru.anlim.rmatch.logic.DBHelper;
import ru.anlim.rmatch.logic.PicaccoHelper;

public class LastMatch extends MainActivity.PlaceholderFragment {

    TextView tvHomeLast, tvGuestLast, tvDateLast, tvLigaLast, tvResultLast;
    ImageView imHomeLast, imGuestLast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_last_match, container, false);

        tvHomeLast = rootView.findViewById(R.id.tvHomeLast);
        tvGuestLast = rootView.findViewById(R.id.tvGuestLast);
        tvDateLast = rootView.findViewById(R.id.tvDateLast);
        tvLigaLast = rootView.findViewById(R.id.tvLigaLast);
        tvResultLast = rootView.findViewById(R.id.tvResultLast);

        imHomeLast = rootView.findViewById(R.id.imHomeLast);
        imGuestLast = rootView.findViewById(R.id.imGuestLast);

        setResult();

        return rootView;
    }

    public void setResult(){

        PicaccoHelper PicaccoHelper = new PicaccoHelper();
        DBHelper dbHelper = new DBHelper(getActivity());
        HashMap<String, String> hashMap = dbHelper.dbReadResult("LastMatch");

        tvHomeLast.     setText(hashMap.get("Home"));
        tvGuestLast.    setText(hashMap.get("Guest"));
        tvDateLast.     setText(hashMap.get("MatchDate"));
        tvLigaLast.     setText(hashMap.get("Tournir"));
        tvResultLast.   setText(hashMap.get("Result"));

        PicaccoHelper.LoadPic(hashMap.get("HomeImage"), imHomeLast);
        PicaccoHelper.LoadPic(hashMap.get("GuestImage"), imGuestLast);
    }
}
