package com.githubrepos.trending.repos.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.githubrepos.trending.common.CoroutineTestRule
import com.githubrepos.trending.repos.api.RepositoriesApi
import com.githubrepos.trending.repos.data.RepositoriesScheduler
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.dao.RepositoriesDao
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoriesRepositoryNewTest {

    val repoApi: RepositoriesApi = mockk()
    val repoDao: RepositoriesDao = mockk(relaxUnitFun = true)
    val scheduler: RepositoriesScheduler = mockk(relaxUnitFun = true)
    val repo: RepositoriesRepositoryNew =
        spyk(RepositoriesRepositoryNew(repoApi, repoDao, scheduler))

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @After
    fun tearDown() {
        unmockkObject(repo, repoApi, repoDao, scheduler)
    }

    @Test
    fun `Method fetch from Remote should call api, clear db and insert data to db`() {

        val list = listOf<Repository>()
        coEvery { repoApi.getRepositories() } returns list

        runBlocking {

            repo.fetchFromRemote()

            coVerifyOrder {
                repoApi.getRepositories()
                repoDao.clearAllRepositories()
                repoDao.insertAllRepositories(list)
                scheduler.scheduleNext()
            }
        }
    }

    @Test
    fun `Method fetch from Remote should schedule PeriodicWorkRequest only once`() {

        val list = listOf<Repository>()
        coEvery { repoApi.getRepositories() } returns list

        runBlocking {

            repo.fetchFromRemote()
            repo.fetchFromRemote()

            coVerify(exactly = 2) {
                repoApi.getRepositories()
                repoDao.clearAllRepositories()
                repoDao.insertAllRepositories(list)
            }

            verify(exactly = 1) { scheduler.scheduleNext() }
        }
    }
}