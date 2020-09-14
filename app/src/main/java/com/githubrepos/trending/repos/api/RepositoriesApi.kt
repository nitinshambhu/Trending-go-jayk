package com.githubrepos.trending.repos.api

import com.githubrepos.trending.repos.data.Repository
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface RepositoriesApi {

    @GET("repositories")
    suspend fun getRepositories(): List<Repository>

    // Rx demonstration purpose only
    @GET("repositories")
    fun getRepositoriesRx(): Single<List<Repository>>
}