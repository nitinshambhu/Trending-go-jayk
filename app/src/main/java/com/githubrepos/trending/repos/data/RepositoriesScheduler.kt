package com.githubrepos.trending.repos.data

import androidx.work.*
import java.util.concurrent.TimeUnit

class RepositoriesScheduler(private val workManager: WorkManager) {

    fun scheduleNext() {
        workManager.enqueueUniquePeriodicWork(
            "RepositoriesSyncWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            getPeriodicWorkRequest()
        )
    }

    fun getPeriodicWorkRequest() = PeriodicWorkRequestBuilder<RepositoriesSyncWorker>(
        2,
        TimeUnit.HOURS
    ).setInitialDelay(2, TimeUnit.HOURS)
        .setConstraints(getNetWorkConstraint())
        .addTag("PERIODIC")
        .build()

    fun getNetWorkConstraint() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun scheduleOnce() {
        workManager.enqueueUniqueWork(
            "RepositoriesSyncWorker",
            ExistingWorkPolicy.REPLACE,
            getOneTimeWorkRequest()
        )
    }

    fun getOneTimeWorkRequest() = OneTimeWorkRequest.Builder(RepositoriesSyncWorker::class.java)
        .setConstraints(getNetWorkConstraint())
        .addTag("ONETIME")
        .build()
}