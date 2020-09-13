package com.githubrepos.trending.repos.data.repository

import com.githubrepos.trending.common.data.ApiResponse
import com.githubrepos.trending.common.data.DataFetchType
import com.githubrepos.trending.common.util.CacheSession
import com.githubrepos.trending.common.util.Feature
import com.githubrepos.trending.common.util.apiResponseFrom
import com.githubrepos.trending.repos.api.RepositoriesApi
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.db.dao.RepositoriesDao
import com.githubrepos.trending.repos.data.STATIC_REPOSITORIES_DATA
import com.githubrepos.trending.common.util.logD
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class RepositoriesRepository(
    private val repoApi: RepositoriesApi,
    private val repoDao: RepositoriesDao,
    private val cacheSession: CacheSession
) {

    suspend fun getRepositories(fetchType: DataFetchType = DataFetchType.Force): ApiResponse<List<Repository>> =
        withContext(Dispatchers.Default) {

            return@withContext apiResponseFrom {

                val redirectRequestToRemote =
                    cacheSession.isTimedOutFor(feature = Feature.GitHubTrendingRepos)
                            || fetchType is DataFetchType.Force

                return@apiResponseFrom if (redirectRequestToRemote) fetchFromRemote()
                else fetchFromDatabase()
            }
        }


    suspend fun fetchFromRemote(): List<Repository> {
        "Fetching from remote ... ".logD("Test===")
        val repositoryList = repoApi.getRepositories()
        "Fetched list size = ${repositoryList.size}".logD("Test===")
        repoDao.clear()
        cacheSession.startTimerFor(Feature.GitHubTrendingRepos)
        repoDao.insertAll(repositoryList)
        return repositoryList
    }

    suspend fun fetchFromDatabase(): List<Repository> {
        "Fetching from Database ... ".logD("Test===")
        return repoDao.all()
    }

    suspend fun fetchFromStaticData(): List<Repository> {
        val collectionType: Type = object : TypeToken<List<Repository>>() {}.type
        return Gson().fromJson(STATIC_REPOSITORIES_DATA, collectionType)
    }

}