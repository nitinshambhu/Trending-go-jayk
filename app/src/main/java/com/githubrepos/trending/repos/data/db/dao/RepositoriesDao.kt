package com.githubrepos.trending.repos.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githubrepos.trending.repos.data.Repository

@Dao
interface RepositoriesDao {

    @Query("SELECT * FROM TrendingRepositories")
    suspend fun all(): List<Repository>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(order: List<Repository>)

    @Query("DELETE FROM TrendingRepositories")
    suspend fun clear()

}