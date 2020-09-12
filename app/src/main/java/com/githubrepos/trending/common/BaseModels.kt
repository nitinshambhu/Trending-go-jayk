package com.githubrepos.trending.common

sealed class DataFetchType {
    object Normal : DataFetchType()
    object Force : DataFetchType()
}

sealed class LoadingState {
    object InProgress : LoadingState()
    object Done : LoadingState()
}