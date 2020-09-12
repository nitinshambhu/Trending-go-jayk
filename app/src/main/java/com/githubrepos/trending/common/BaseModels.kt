package com.githubrepos.trending.common

sealed class DataFetchType {
    object Normal : DataFetchType()
    object Force : DataFetchType()
}

sealed class LoadingState {
    object InProgress : LoadingState()
    object Done : LoadingState()
}

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    class Error<out T>(message: String) : ApiResponse<T>()
}