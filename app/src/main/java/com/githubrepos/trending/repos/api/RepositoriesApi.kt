package com.githubrepos.trending.repos.api

import com.githubrepos.trending.repos.data.Repository
import retrofit2.http.GET

interface RepositoriesApi {

    @GET("repositories")
    suspend fun getRepositories(): List<Repository>
}