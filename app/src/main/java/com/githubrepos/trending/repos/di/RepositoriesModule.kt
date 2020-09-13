package com.githubrepos.trending.repos.di

import com.githubrepos.trending.repos.MainActivity
import com.githubrepos.trending.common.db.AppDatabase
import com.githubrepos.trending.repos.api.RepositoriesApi
import com.githubrepos.trending.repos.data.repository.RepositoriesRepository
import com.githubrepos.trending.repos.ui.RepositoriesAdapter
import com.githubrepos.trending.repos.ui.RepositoriesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val repositoriesModule = module {
    scope<MainActivity> {
        scoped { RepositoriesAdapter(context = androidContext()) }
        scoped { provideRepositoriesApi(retrofit = get()) }
        scoped { provideRepositoriesDao(appDatabase = get()) }
        scoped { RepositoriesRepository(repoApi = get(), repoDao = get(), cacheSession = get()) }
        viewModel { RepositoriesViewModel(repo = get()) }
    }
}

fun provideRepositoriesApi(retrofit: Retrofit) =
    retrofit.create(RepositoriesApi::class.java)

fun provideRepositoriesDao(appDatabase: AppDatabase) = appDatabase.repositoriesDao()