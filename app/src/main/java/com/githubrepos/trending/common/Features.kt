package com.githubrepos.trending.common

sealed class Feature(val name: String, val timeOutInMinutes: Long) {
    object GitHubTrendingRepos : Feature(name = "TrendingRepos", timeOutInMinutes = 2)
}