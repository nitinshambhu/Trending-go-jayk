package com.githubrepos.trending.repos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TrendingRepositories")
data class Repository(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val author: String = "",
    val name: String = "",
    val avatar: String = "",
    val description: String = "",
    val language: String? = "",
    val stars: String = "",
    val forks: String = ""
)