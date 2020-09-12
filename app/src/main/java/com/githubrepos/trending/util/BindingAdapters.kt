package com.githubrepos.trending.util

import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.githubrepos.trending.R
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    Picasso.get().apply {
        if (url.isNullOrBlank()) {
            load(R.drawable.ic_avatar_placeholder).into(view)
        } else {
            load(url)
                .placeholder(
                    ResourcesCompat.getDrawable(
                        view.resources, R.drawable.ic_avatar_placeholder,
                        null
                    )!!
                ).into(view)
        }
    }
}