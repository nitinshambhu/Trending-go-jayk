package com.githubrepos.trending.common

sealed class Feature(val name: String, val timeOutInMinutes: Int) {
    object GitHubTrendingRepos : Feature(name = "TrendingRepos", timeOutInMinutes = 2 * HOUR_IN_MINUTES)
}