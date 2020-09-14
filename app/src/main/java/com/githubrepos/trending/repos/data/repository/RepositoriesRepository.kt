package com.githubrepos.trending.repos.data.repository

import com.githubrepos.trending.common.data.ApiResponse
import com.githubrepos.trending.common.data.DataFetchType
import com.githubrepos.trending.common.util.*
import com.githubrepos.trending.repos.api.RepositoriesApi
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.dao.RepositoriesDao
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoriesRepository(
    private val repoApi: RepositoriesApi,
    private val repoDao: RepositoriesDao,
    private val cacheSession: CacheSession
) {

    suspend fun getRepositories(fetchType: DataFetchType = DataFetchType.Normal): ApiResponse<List<Repository>> =
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
        repoDao.clearAllRepositories()
        cacheSession.startTimerFor(Feature.GitHubTrendingRepos)
        repoDao.insertAllRepositories(repositoryList)
        return repositoryList
    }

    suspend fun fetchFromDatabase(): List<Repository> {
        "Fetching from Database ... ".logD("Test===")
        return repoDao.allRepositories()
    }

    // Rx demonstration purpose only
    fun getRepositoriesRx(fetchType: DataFetchType = DataFetchType.Normal): Single<List<Repository>> {

        val redirectRequestToRemote =
            cacheSession.isTimedOutFor(feature = Feature.GitHubTrendingRepos)
                    || fetchType is DataFetchType.Force

        return if (!redirectRequestToRemote) {
            Single.fromCallable { repoDao.allRepositoriesRx() }
                .subscribeOn(Schedulers.newThread())
        } else {
            repoApi.getRepositoriesRx()
                .subscribeOn(Schedulers.newThread())
                .onErrorReturnItem(listOf())
                .flatMap { list ->
                    if (list.isNotEmpty()) {
                        repoDao.clearAllRepositoriesRx()
                        repoDao.insertAllRepositoriesRx(list)
                        return@flatMap Single.just(list)
                    } else {
                        return@flatMap Single.error<List<Repository>>(Throwable("Test Message"))
                    }
                }
        }
    }

}