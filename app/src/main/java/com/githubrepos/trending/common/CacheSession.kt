package com.githubrepos.trending.common

import android.content.SharedPreferences
import android.os.SystemClock
import androidx.core.content.edit
import java.util.concurrent.TimeUnit

class CacheSession(private val preferences: SharedPreferences) {

    fun startFor(feature: Feature) {
        preferences.edit(commit = true) {
            putLong(feature.name, getCurrentTime())
        }
    }

    fun isTimedOutFor(feature: Feature): Boolean {
        val savedTimestamp = preferences.getLong(feature.name, 0L)
        val timeoutConfigured = TimeUnit.HOURS.toMillis(feature.timeOutInMinutes.toLong())
        return (getCurrentTime() - savedTimestamp) >= timeoutConfigured
    }

    fun getCurrentTime() = SystemClock.elapsedRealtime()
}