package com.githubrepos.trending.common

sealed class DataFetchType {
    object Normal : DataFetchType()
    object Force : DataFetchType()
}