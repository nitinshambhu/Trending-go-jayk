package com.githubrepos.trending.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.githubrepos.trending.R
import com.githubrepos.trending.databinding.LayoutActivityTitleBinding

abstract class BaseActivity : AppCompatActivity() {

    val titleBinding: LayoutActivityTitleBinding by lazy {
        LayoutActivityTitleBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.customView = titleBinding.root
        setTitle(R.string.app_name)
    }

    override fun setTitle(titleId: Int) {
        titleBinding.title.setText(R.string.app_name)
    }

}