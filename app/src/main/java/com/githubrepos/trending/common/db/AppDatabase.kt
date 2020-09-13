package com.githubrepos.trending.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.dao.RepositoriesDao

@Database(entities = [Repository::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repositoriesDao(): RepositoriesDao

    companion object {

        private var instance: AppDatabase? = null

        @Synchronized
        fun get(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, "TrendingRepositoriesDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }

}