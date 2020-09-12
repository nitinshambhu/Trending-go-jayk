package com.githubrepos.trending.repos.data.repository

import com.githubrepos.trending.common.CacheSession
import com.githubrepos.trending.common.DataFetchType
import com.githubrepos.trending.common.Feature
import com.githubrepos.trending.repos.api.RepositoriesApi
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.db.dao.RepositoriesDao
import com.githubrepos.trending.repos.data.STATIC_REPOSITORIES_DATA
import com.githubrepos.trending.util.logD
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

    suspend fun getRepositories(fetchType: DataFetchType = DataFetchType.Force): List<Repository> {
        return withContext(Dispatchers.Default) {

            if (cacheSession.isTimedOutFor(Feature.GitHubTrendingRepos) || fetchType is DataFetchType.Force) {
                val fetchedFinal = fetchFromRemote()
                "fetchedFinal from remote .... $fetchedFinal".logD("Test===")
                return@withContext fetchedFinal
            } else {
                val fetchedFinal = fetchFromDatabase()
                "fetchedFinal from DB.... $fetchedFinal".logD("Test===")
                return@withContext fetchedFinal
            }

        }
    }

    suspend fun fetchFromRemote(): List<Repository> {
        val repositoryList = repoApi.getRepositories()
        "repositoryList received size = ${repositoryList.size}".logD("Test===")

        return if (repositoryList.isNotEmpty()) {
            repoDao.clear()
            "Dao cleared ....".logD("Test===")
            cacheSession.startFor(Feature.GitHubTrendingRepos)
            "started cache session ....".logD("Test===")
            repoDao.insertAll(repositoryList)
            "inserted all ....".logD("Test===")
            repositoryList
        } else {
            fetchFromDatabase()
        }
    }

    suspend fun fetchFromDatabase(): List<Repository> = repoDao.all()

    suspend fun fetchFromStaticData(): List<Repository> {
        val collectionType: Type = object : TypeToken<List<Repository>>() {}.type
        return Gson().fromJson(STATIC_REPOSITORIES_DATA, collectionType)
    }

}