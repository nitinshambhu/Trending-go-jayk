package com.githubrepos.trending.repos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githubrepos.trending.repos.data.Repository

@Dao
interface RepositoriesDao {

    @Query("SELECT * FROM TrendingRepositories")
    suspend fun allRepositories(): List<Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRepositories(repositories: List<Repository>)

    @Query("DELETE FROM TrendingRepositories")
    suspend fun clearAllRepositories()

    // Rx demonstration purpose only
    @Query("SELECT * FROM TrendingRepositories")
    fun allRepositoriesRx(): List<Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRepositoriesRx(repositories: List<Repository>)

    @Query("DELETE FROM TrendingRepositories")
    fun clearAllRepositoriesRx()
}