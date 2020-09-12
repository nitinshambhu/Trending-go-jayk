package com.githubrepos.trending

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.githubrepos.trending.common.BaseActivity
import com.githubrepos.trending.repos.di.repositoriesModule
import com.githubrepos.trending.repos.ui.RepositoriesFragment
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(repositoriesModule)
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, RepositoriesFragment())
            .commit()
    }

    override fun onDestroy() {
        unloadKoinModules(repositoriesModule)
        super.onDestroy()
    }
}