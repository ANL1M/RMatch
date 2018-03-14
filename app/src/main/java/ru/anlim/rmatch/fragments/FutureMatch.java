package ru.anlim.rmatch.fragments;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import ru.anlim.rmatch.MainActivity;
import ru.anlim.rmatch.R;
import ru.anlim.rmatch.logic.DBHelper;
import ru.anlim.rmatch.logic.JsoupHelper;
import ru.anlim.rmatch.logic.PicaccoHelper;

public class FutureMatch extends MainActivity.PlaceholderFragment implements SwipeRefreshLayout.OnRefreshListener {

    TextView tvHomeFuture, tvGuestFuture, tvDateFuture, tvLigaFuture, tvVSFuture;
    ImageView imHomeFuture, imGuestFuture;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_future_match, container, false);

        tvHomeFuture = rootView.findViewById(R.id.tvHomeFuture);
        tvGuestFuture = rootView.findViewById(R.id.tvGuestFuture);
        tvDateFuture = rootView.findViewById(R.id.tvDateFuture);
        tvLigaFuture = rootView.findViewById(R.id.tvLigaFuture);
        tvVSFuture = rootView.findViewById(R.id.tvVSFuture);

        imHomeFuture = rootView.findViewById(R.id.imHomeFuture);
        imGuestFuture = rootView.findViewById(R.id.imGuestFuture);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.real_blue,
                R.color.real_red,
                R.color.real_yellow);

        setResult();

        return rootView;
    }

    public void setResult(){

        PicaccoHelper PicaccoHelper = new PicaccoHelper();
        DBHelper dbHelper = new DBHelper(getActivity());
        HashMap<String, String> hashMap = dbHelper.dbReadResult("FutureMatch");

        tvHomeFuture.   setText(hashMap.get("Home"));
        tvGuestFuture.  setText(hashMap.get("Guest"));
        tvDateFuture.   setText(hashMap.get("MatchDate"));
        tvLigaFuture.   setText(hashMap.get("Tournir"));
        tvVSFuture.     setText(hashMap.get("Result"));

        PicaccoHelper.LoadPic(hashMap.get("HomeImage"), imHomeFuture);
        PicaccoHelper.LoadPic(hashMap.get("GuestImage"), imGuestFuture);
    }

    @Override
    public void onRefresh() {
        JsoupHelper jsoupHelper = new JsoupHelper(this);
        jsoupHelper.execute(101);
    }

    public void onToast(){
        Toast.makeText(getActivity(), "TOsta", Toast.LENGTH_SHORT).show();
    }
}
