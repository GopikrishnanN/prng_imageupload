package com.prng.androidimageuploadcomponent.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.prng.androidimageuploadcomponent.R
import java.util.*

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context).load(url).error(R.drawable.ic_launcher_foreground).into(view)
    }
}