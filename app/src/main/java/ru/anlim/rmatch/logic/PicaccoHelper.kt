package ru.anlim.rmatch.logic;

import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class PicaccoHelper {

    public void LoadPic(String url, ImageView imageView){

        if (url != null){
            Picasso.get()
                    .load(url.trim())
                    .noFade()
                    .into(imageView);
        }
    }
}
