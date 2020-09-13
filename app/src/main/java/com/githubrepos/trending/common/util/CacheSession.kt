package com.githubrepos.trending.common.util

import android.content.SharedPreferences
import android.os.SystemClock
import androidx.core.content.edit

class CacheSession(private val preferences: SharedPreferences) {

    fun startTimerFor(feature: Feature) {
        preferences.edit(commit = true) {
            putLong(feature.name, getCurrentTimestamp())
        }
    }

    fun isTimedOutFor(feature: Feature): Boolean {
        val savedTimestamp = preferences.getLong(feature.name, 0L)
        val timeoutConfigured = feature.timeOutInMillis
        return (getCurrentTimestamp() - savedTimestamp) <= timeoutConfigured
    }

    fun getCurrentTimestamp() = SystemClock.elapsedRealtime()
}