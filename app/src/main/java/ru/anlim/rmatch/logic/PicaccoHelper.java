package ru.anlim.rmatch.logic;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicaccoHelper {

    public void LoadPic(String url, ImageView iamgeView){

        Picasso.get()
                .load(url)
                .into(iamgeView);
    }
}
