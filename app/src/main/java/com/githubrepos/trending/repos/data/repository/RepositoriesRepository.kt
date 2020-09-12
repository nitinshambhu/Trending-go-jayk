package com.githubrepos.trending.repos.data.repository

import com.githubrepos.trending.common.DataFetchType
import com.githubrepos.trending.repos.api.RepositoriesApi
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.staticRepositoriesData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RepositoriesRepository(val repoApi: RepositoriesApi) {

    fun getRepositories(fetchType: DataFetchType = DataFetchType.Force): List<Repository> {
        return fetchFromStaticData()
    }

    fun fetchFromStaticData(): List<Repository> {
        val collectionType: Type = object : TypeToken<List<Repository>>() {}.type
        return Gson().fromJson(staticRepositoriesData, collectionType)
    }

}