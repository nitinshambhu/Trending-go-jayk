package com.githubrepos.trending.common.di

import android.content.Context
import android.content.SharedPreferences
import com.githubrepos.trending.common.util.CacheSession
import com.githubrepos.trending.common.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PREFERENCES_NAME = "com.github.trending"

val appModule = module {
    single { AppDatabase.get(context = androidContext()) }
    single { provideSettingsPreferences(context = androidContext()) }
    single { CacheSession(preferences = get()) }
}

private fun provideSettingsPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)