package com.githubrepos.trending.common.di

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.githubrepos.trending.common.util.CacheSession
import com.githubrepos.trending.common.db.AppDatabase
import com.githubrepos.trending.repos.data.RepositoriesScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PREFERENCES_NAME = "com.github.trending"

/**
 *  An app module that will help inject all the app level dependencies
 */
val appModule = module {
    single { AppDatabase.get(context = androidContext()) }
    single { provideSettingsPreferences(context = androidContext()) }
    single { CacheSession(preferences = get()) }
    single { WorkManager.getInstance(get()) }
    single { RepositoriesScheduler(workManager = get()) }
}

private fun provideSettingsPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)