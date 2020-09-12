package com.githubrepos.trending.common

import android.content.SharedPreferences
import android.os.SystemClock
import androidx.core.content.edit
import com.githubrepos.trending.util.logD
import java.util.concurrent.TimeUnit

class CacheSession(private val preferences: SharedPreferences) {

    fun startFor(feature: Feature) {
        preferences.edit(commit = true) {
            putLong(feature.name, SystemClock.elapsedRealtime())
        }
    }

    fun isTimedOutFor(feature: Feature): Boolean {
        val savedTimestamp = preferences.getLong(feature.name, 0L)
        val timeoutConfigured = TimeUnit.MINUTES.toMillis(feature.timeOutInMinutes)
        val currentTimestamp = SystemClock.elapsedRealtime()
        "savedTimestamp ==== $savedTimestamp".logD("Test===")
        "currentTimestamp ==== $currentTimestamp".logD("Test===")
        "timeoutConfigured ==== $timeoutConfigured".logD("Test===")
        "Difference in time ==== ${currentTimestamp - savedTimestamp}".logD("Test===")
        return (currentTimestamp - savedTimestamp) >= timeoutConfigured
    }
}