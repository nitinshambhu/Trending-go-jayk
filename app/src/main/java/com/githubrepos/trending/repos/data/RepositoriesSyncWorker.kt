package com.githubrepos.trending.repos.data

import android.content.Context
import androidx.work.*
import com.githubrepos.trending.common.util.logD
import com.githubrepos.trending.repos.data.repository.RepositoriesRepositoryNew
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.UnknownHostException

class RepositoriesSyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val repo: RepositoriesRepositoryNew by inject()

    override suspend fun doWork(): Result {
        "RepositoriesSyncWorker doWork()... $tags".logD("Test===")
        return try {
            repo.fetchFromRemote()
            Result.success()
        } catch (e: UnknownHostException) {
            Result.retry()
        }
    }
}