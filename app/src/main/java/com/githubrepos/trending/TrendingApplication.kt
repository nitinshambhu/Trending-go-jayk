package com.githubrepos.trending

import android.app.Application
import com.githubrepos.trending.common.di.appModule
import com.githubrepos.trending.common.di.networkModule
import com.githubrepos.trending.repos.di.repositoriesBackgroundModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidApp
class TrendingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appLevelModules = listOf(appModule, networkModule, repositoriesBackgroundModule)
        startKoin {
            androidContext(this@TrendingApplication)
            modules(appLevelModules)
        }
    }
}