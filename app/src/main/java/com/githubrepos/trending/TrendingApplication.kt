package com.githubrepos.trending

import android.app.Application
import com.githubrepos.trending.common.di.appModule
import com.githubrepos.trending.common.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TrendingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appLevelModules = listOf(appModule, networkModule)
        startKoin {
            androidContext(this@TrendingApplication)
            modules(appLevelModules)
        }
    }
}