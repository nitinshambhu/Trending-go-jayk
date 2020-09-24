package com.githubrepos.trending.repos.data.repository

import androidx.lifecycle.LiveData
import com.githubrepos.trending.common.DEBUG_TAG
import com.githubrepos.trending.common.util.logD
import com.githubrepos.trending.repos.api.RepositoriesApi
import com.githubrepos.trending.repos.data.RepositoriesScheduler
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.dao.RepositoriesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoriesRepositoryNew(
    private val repoApi: RepositoriesApi,
    private val repoDao: RepositoriesDao,
    private val scheduler: RepositoriesScheduler
) {

    private var schedulingNecessary = true

    suspend fun fetchFromRemote() {
        withContext(Dispatchers.Default) {
            val repositoryList = repoApi.getRepositories()
            "repositoryList received size = ${repositoryList.size}".logD(DEBUG_TAG)
            repoDao.clearAllRepositories()
            "Dao cleared ....".logD(DEBUG_TAG)
            repoDao.insertAllRepositories(repositoryList)
            "inserted all ....".logD(DEBUG_TAG)
            if(schedulingNecessary) {
                scheduler.scheduleNext()
                "Scheduled next ....".logD(DEBUG_TAG)
                schedulingNecessary = false
            }
        }
    }

    fun fetchFromDatabase(): LiveData<List<Repository>> =  repoDao.allRepositoriesAsLiveData()


}