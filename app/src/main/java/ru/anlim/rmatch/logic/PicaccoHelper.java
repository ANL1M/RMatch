package ru.anlim.rmatch.logic;

import android.widget.ImageView;
import android.widget.MediaController;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class PicaccoHelper {

    public void LoadPic(String url, ImageView imageView){

        Picasso.get()
                .load(url.trim())
                //.networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView);
    }
}
