package com.githubrepos.trending.common.util

import com.githubrepos.trending.common.data.ApiResponse

fun <T> T.asSuccess(): ApiResponse<T> = ApiResponse.Success(this)

fun <T> Exception.asError(): ApiResponse<T> {
    printStackTrace()
    return ApiResponse.Error(this.message!!)
}

suspend fun <T> apiResponseFrom(block: suspend () -> T): ApiResponse<T> {
    return try {
        block.invoke().asSuccess()
    } catch (e: Exception) {
        e.asError()
    }
}

fun <T> apiResponseHandler(
    status: ApiResponse<T>,
    onSuccess: (T) -> Unit,
    onError: () -> Unit
) {
    when (status) {
        is ApiResponse.Success -> onSuccess.invoke(status.data)
        is ApiResponse.Error -> onError.invoke()
    }

}