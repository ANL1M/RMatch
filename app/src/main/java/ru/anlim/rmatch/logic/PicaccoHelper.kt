package ru.anlim.rmatch.logic

import android.widget.ImageView
import com.squareup.picasso.Picasso

class PicaccoHelper {
    fun loadPic(url: String?, imageView: ImageView?) {
        if (url != null) {
            Picasso.get()
                .load(url.trim { it <= ' ' })
                .noFade()
                .into(imageView)
        }
    }
}