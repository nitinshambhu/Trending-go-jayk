package com.githubrepos.trending.common.di

import android.content.Context
import com.githubrepos.trending.R
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *  A network module that will help inject all the network dependencies across all features/app
 */
val networkModule = module {
    factory { provideOkHttpClient() }
    single { provideRetrofit(okHttpClient = get(), context = androidContext()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient, context : Context): Retrofit {
    return Retrofit.Builder()
        .baseUrl(context.getString(R.string.base_url))
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}

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