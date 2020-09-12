package com.githubrepos.trending.di

import android.content.Context
import android.content.SharedPreferences
import com.githubrepos.trending.common.CacheSession
import com.githubrepos.trending.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PREFERENCES_FILE_KEY = "com.github.trending"

val appModule = module {
    single { AppDatabase.get(context = androidContext()) }
    single { provideSettingsPreferences(context = androidContext()) }
    single { CacheSession(preferences = get()) }
}

private fun provideSettingsPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)