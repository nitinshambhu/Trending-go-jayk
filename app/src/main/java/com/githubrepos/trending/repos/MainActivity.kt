package com.githubrepos.trending.repos

import android.os.Bundle
import android.os.PersistableBundle
import com.githubrepos.trending.common.BaseActivity
import com.githubrepos.trending.common.DEBUG_TAG
import com.githubrepos.trending.common.util.logD
import com.githubrepos.trending.repos.di.repositoriesModule
import com.githubrepos.trending.repos.ui.hilt.RepositoriesFragment
import dagger.hilt.android.AndroidEntryPoint
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(repositoriesModule)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, RepositoriesFragment())
                .commit()
        }
    }

    override fun onDestroy() {
        unloadKoinModules(repositoriesModule)
        super.onDestroy()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        "MainActivity :: onRestoreInstanceState ==> $savedInstanceState".logD(DEBUG_TAG)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        "MainActivity :: onSaveInstanceState ==> $outState".logD(DEBUG_TAG)
    }
}