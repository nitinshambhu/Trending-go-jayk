package com.githubrepos.trending.common.util

import java.util.concurrent.TimeUnit

sealed class Feature(val name: String, val timeOutInMillis: Long) {
    object GitHubTrendingRepos : Feature(name = "TrendingRepos", timeOutInMillis = TimeUnit.HOURS.toMillis(2 ))
}