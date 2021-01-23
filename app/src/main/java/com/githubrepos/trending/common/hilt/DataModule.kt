package com.githubrepos.trending.common.hilt

import android.content.Context
import com.githubrepos.trending.R
import com.githubrepos.trending.common.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, @ApplicationContext context : Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun providesRepositoriesDao(@ApplicationContext context: Context) = AppDatabase.get(context = context)
}