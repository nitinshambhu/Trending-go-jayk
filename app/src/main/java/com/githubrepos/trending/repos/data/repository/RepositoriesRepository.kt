package com.githubrepos.trending.repos.data.repository

import com.githubrepos.trending.common.DataFetchType
import com.githubrepos.trending.repos.api.RepositoriesApi
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.staticRepositoriesData
import com.githubrepos.trending.util.logD
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class RepositoriesRepository(val repoApi: RepositoriesApi) {

    suspend fun getRepositories(fetchType: DataFetchType = DataFetchType.Force): List<Repository> {
        return withContext(Dispatchers.Default){
            return@withContext fetchFromRemote()
        }
    }

    suspend fun fetchFromRemote() : List<Repository> {
        val repositoryList = repoApi.getRepositories()
        "repositoryList received size = ${repositoryList.size}".logD("Test===")
        return repositoryList
    }

    suspend fun fetchFromStaticData(): List<Repository> {
        val collectionType: Type = object : TypeToken<List<Repository>>() {}.type
        return Gson().fromJson(staticRepositoriesData, collectionType)
    }

}