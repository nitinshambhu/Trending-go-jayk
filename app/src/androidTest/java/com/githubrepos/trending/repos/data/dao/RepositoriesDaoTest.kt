package com.githubrepos.trending.repos.data.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner

import com.githubrepos.trending.common.db.AppDatabase
import com.githubrepos.trending.repos.data.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
open class RepositoriesDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var repositoriesDao : RepositoriesDao

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        repositoriesDao = appDatabase.repositoriesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    fun getRepositoryList() = listOf(
        Repository(id = 1, author = "Test1"),
        Repository(id = 2, author = "Test2")
    )

    @Test
    fun insertAllMethodShouldInsertAllProvidedEntities() {

        runBlockingTest {

            repositoriesDao.insertAll(getRepositoryList())

            val repos = repositoriesDao.all()

            assertTrue(repos.isNotEmpty())
            assertEquals(2, repos.size)
        }

    }

    @Test
    fun clearMethodShouldDeleteAllEntitiesAddedByInsertAllMethod() {

        runBlockingTest {

            // confirmation of initial stage
            repositoriesDao.insertAll(getRepositoryList())
            val repos = repositoriesDao.all()
            assertTrue(repos.isNotEmpty())
            assertEquals(2, repos.size)

            // action
            repositoriesDao.clear()

            //verification
            val emptyRepos = repositoriesDao.all()
            assertTrue(emptyRepos.isEmpty())
            assertEquals(0, emptyRepos.size)
        }

    }


}