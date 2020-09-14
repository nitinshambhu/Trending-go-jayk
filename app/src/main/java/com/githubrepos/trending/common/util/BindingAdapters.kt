package com.githubrepos.trending.common.util

import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.githubrepos.trending.R
import com.squareup.picasso.Picasso

/**
 *  Sets the image from url for any ImageView
 */
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