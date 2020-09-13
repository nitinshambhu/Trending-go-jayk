package com.githubrepos.trending.repos.ui.repository

import com.githubrepos.trending.common.data.DataFetchType
import com.githubrepos.trending.common.util.CacheSession
import com.githubrepos.trending.common.util.Feature
import com.githubrepos.trending.common.util.asSuccess
import com.githubrepos.trending.repos.api.RepositoriesApi
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.db.dao.RepositoriesDao
import com.githubrepos.trending.repos.data.repository.RepositoriesRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoriesRepositoryTest {

    val repoApi: RepositoriesApi = mockk()
    val repoDao: RepositoriesDao = mockk(relaxUnitFun = true)
    val cacheSession: CacheSession = mockk(relaxUnitFun = true)
    val repo: RepositoriesRepository = spyk(RepositoriesRepository(repoApi, repoDao, cacheSession))

    @After
    fun tearDown() {
        unmockkObject(repo, repoApi, repoDao, cacheSession)
    }

    @Test
    fun `Method fetch from DB should call dao and not remote api`() {
        val list = listOf<Repository>()
        coEvery { repoDao.all() } returns list

        runBlockingTest {

            repo.fetchFromDatabase()

            coVerify(exactly = 1) { repoDao.all() }
            coVerify(exactly = 0) { repoApi.getRepositories() }
        }
    }

    @Test
    fun `Method fetch from Remote should call api, clear db and insert data to db`() {

        val list = listOf<Repository>()
        coEvery { repoApi.getRepositories() } returns list

        runBlockingTest {

            val result = repo.fetchFromRemote()

            assertEquals(result, list)

            coVerifyOrder {
                repoApi.getRepositories()
                repoDao.clear()
                cacheSession.startTimerFor(Feature.GitHubTrendingRepos)
                repoDao.insertAll(list)
            }
            coVerify(exactly = 0) { repoDao.all() }
        }
    }

    @Test
    fun `Method getRepositories should fetch data from DB if the fetch type is  normal and cacheSession has NOT timedout`() {

        val list = listOf<Repository>()

        coEvery { cacheSession.isTimedOutFor(any()) } returns false
        coEvery { repo.fetchFromRemote() } returns list
        coEvery { repo.fetchFromDatabase() } returns list

        runBlocking{

            val result = repo.getRepositories(DataFetchType.Normal)

            assertEquals(list.asSuccess(), result)

            coVerify(exactly = 1) { repo.fetchFromDatabase() }
            coVerify(exactly = 0) { repo.fetchFromRemote() }
        }
    }

    @Test
    fun `Method getRepositories should fetch data from DB if the fetch type is normal and cacheSession has timedout`() {

        val list = listOf<Repository>()

        coEvery { cacheSession.isTimedOutFor(any()) } returns true
        coEvery { repo.fetchFromRemote() } returns list
        coEvery { repo.fetchFromDatabase() } returns list

        runBlocking{

            val result = repo.getRepositories(DataFetchType.Normal)

            assertEquals(list.asSuccess(), result)

            coVerify(exactly = 1) { repo.fetchFromRemote() }
            coVerify(exactly = 0) { repo.fetchFromDatabase() }
        }
    }

    @Test
    fun `Method getRepositories should fetch data from api if the fetch type is Force and cacheSession has NOT timedout`() {

        val list = listOf<Repository>()

        coEvery { cacheSession.isTimedOutFor(any()) } returns false
        coEvery { repo.fetchFromRemote() } returns list
        coEvery { repo.fetchFromDatabase() } returns list

        runBlocking{

            val result = repo.getRepositories(DataFetchType.Force)

            assertEquals(list.asSuccess(), result)

            coVerify(exactly = 1) { repo.fetchFromRemote() }
            coVerify(exactly = 0) { repo.fetchFromDatabase() }
        }
    }

    @Test
    fun `Method getRepositories should fetch data from api if the fetch type is Force and cacheSession has timedout`() {

        val list = listOf<Repository>()

        coEvery { cacheSession.isTimedOutFor(any()) } returns true
        coEvery { repo.fetchFromRemote() } returns list
        coEvery { repo.fetchFromDatabase() } returns list

        runBlocking{

            val result = repo.getRepositories(DataFetchType.Force)

            assertEquals(list.asSuccess(), result)

            coVerify(exactly = 1) { repo.fetchFromRemote() }
            coVerify(exactly = 0) { repo.fetchFromDatabase() }
        }
    }
}