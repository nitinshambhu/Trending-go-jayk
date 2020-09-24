package com.githubrepos.trending.common.data

import androidx.work.*
import com.githubrepos.trending.repos.data.RepositoriesScheduler
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class RepositoriesSchedulerTest {

    val workManager: WorkManager = mockk(relaxUnitFun = true)
    val scheduler: RepositoriesScheduler = spyk(RepositoriesScheduler(workManager = workManager))

    @Test
    fun `Schedule getConstraint should return a constraint with NetworkType CONNECTED set to it`() {

        val constraints = scheduler.getNetWorkConstraint()

        assertEquals(NetworkType.CONNECTED, constraints.requiredNetworkType)

    }

    @Test
    fun `Schedule getPeriodicWorkRequest should return a periodicWorkRequest with delay set to 2 hours`() {

        val periodicWorkRequest = scheduler.getPeriodicWorkRequest()

        assertEquals(TimeUnit.HOURS.toMillis(2), periodicWorkRequest.workSpec.initialDelay)
        assertEquals(TimeUnit.HOURS.toMillis(2), periodicWorkRequest.workSpec.intervalDuration)
        assertEquals(
            NetworkType.CONNECTED,
            periodicWorkRequest.workSpec.constraints.requiredNetworkType
        )
    }

    @Test
    fun `Schedule next should schedule a periodic work request`() {

        val periodicWorkRequest = mockk<PeriodicWorkRequest>(relaxUnitFun = true)

        every { scheduler.getPeriodicWorkRequest() } returns periodicWorkRequest
        every { workManager.enqueueUniquePeriodicWork(any(), any(), any()) } returns mockk()

        scheduler.scheduleNext()

        verify {
            scheduler.getPeriodicWorkRequest()
            workManager.enqueueUniquePeriodicWork(
                "RepositoriesSyncWorker",
                ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest
            )
        }
    }

    @Test
    fun `Schedule next should schedule a one time work request`() {

        val oneTimeWorkRequest = mockk<OneTimeWorkRequest>(relaxUnitFun = true)
        every { scheduler.getOneTimeWorkRequest() } returns oneTimeWorkRequest

        every {
            workManager.enqueueUniqueWork(
                "RepositoriesSyncWorker",
                ExistingWorkPolicy.REPLACE,
                oneTimeWorkRequest
            )
        } returns mockk()

        scheduler.scheduleOnce()

        verify {
            workManager.enqueueUniqueWork(
                "RepositoriesSyncWorker",
                ExistingWorkPolicy.REPLACE, oneTimeWorkRequest
            )
        }
    }
}