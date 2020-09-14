package com.githubrepos.trending.common.util

import android.content.SharedPreferences
import android.os.SystemClock
import androidx.core.content.edit

/**
 *  Tracks the cache timer for a give feature
 */
class CacheSession(private val preferences: SharedPreferences) {

    /**
     *  Saves the currentTimestamp when called. Call this when a feature wants to start keeping track of time
     */
    fun startTimerFor(feature: Feature) {
        preferences.edit(commit = true) {
            putLong(feature.name, getCurrentTimestamp())
        }
    }

    /**
     *  Returns true if the timestamp of a given feature is timed out or have not even started
     *  keeping track of time
     */
    fun isTimedOutFor(feature: Feature): Boolean {
        val savedTimestamp = preferences.getLong(feature.name, 0L)
        val timeoutConfigured = feature.timeOutInMillis
        return (getCurrentTimestamp() - savedTimestamp) >= timeoutConfigured || savedTimestamp == 0L
    }

    fun getCurrentTimestamp() = SystemClock.elapsedRealtime()
}