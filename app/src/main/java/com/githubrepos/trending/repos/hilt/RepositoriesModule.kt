package com.githubrepos.trending.repos.hilt

import com.githubrepos.trending.common.db.AppDatabase
import com.githubrepos.trending.repos.api.RepositoriesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoriesModule {

    @Provides
    @Singleton
    fun provideRepositoriesApi(retrofit: Retrofit) : RepositoriesApi = retrofit.create(RepositoriesApi::class.java)

    @Provides
    @Singleton
    fun provideRepositoriesDao(appDatabase: AppDatabase) = appDatabase.repositoriesDao()
}