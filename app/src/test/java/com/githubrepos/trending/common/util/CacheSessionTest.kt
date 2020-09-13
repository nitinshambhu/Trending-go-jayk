package com.githubrepos.trending.common.util

import android.content.SharedPreferences
import android.os.SystemClock
import com.githubrepos.trending.common.BaseTest
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CacheSessionTest : BaseTest() {

    @MockK
    lateinit var preferences: SharedPreferences

    @MockK
    lateinit var editor: SharedPreferences.Editor

    @InjectMockKs()
    lateinit var cacheSession: CacheSession

    override fun setUp() {
        super.setUp()
        mockkStatic(SystemClock::class)
    }

    fun setPreferencesEditor() {
        every { preferences.edit() } returns editor
        editor.apply {
            every { putLong(any(), any()) } returns editor
            every { commit() } returns true
        }
    }

    @Test
    fun `Starting cache session should store the current timestamp with current feature as the key`() {
        setPreferencesEditor()

        every { SystemClock.elapsedRealtime() } returns 123

        cacheSession.startTimerFor(Feature.GitHubTrendingRepos)

        verifyOrder {
            editor.putLong(Feature.GitHubTrendingRepos.name, 123)
            editor.commit()
        }
    }

    @Test
    fun `Starting cache session check timeout method returns true when the time difference is more than timeout`() {
        val feature = mockk<Feature>().apply {
            every { name } returns "Test"
            every { timeOutInMillis } returns 10
        }

        every { SystemClock.elapsedRealtime() } returns 40 //current timestamp
        every { preferences.getLong(any(), any()) } returns 20 //saved timestamp

        assertTrue(cacheSession.isTimedOutFor(feature = feature))

        verify {
            preferences.getLong("Test", 0L)
        }
    }

    @Test
    fun `Starting cache session check timeout method returns false when the time difference is less than timeout`() {
        val feature = mockk<Feature>().apply {
            every { name } returns "Test"
            every { timeOutInMillis } returns 10
        }

        every { SystemClock.elapsedRealtime() } returns 40 //current timestamp
        every { preferences.getLong(any(), any()) } returns 35 //saved timestamp

        assertFalse(cacheSession.isTimedOutFor(feature = feature))

        verify {
            preferences.getLong("Test", 0L)
        }
    }

    @Test
    fun `Starting cache session check timeout method returns true when the time difference is equal timeout`() {
        val feature = mockk<Feature>().apply {
            every { name } returns "Test"
            every { timeOutInMillis } returns 10
        }

        every { SystemClock.elapsedRealtime() } returns 40 //current timestamp
        every { preferences.getLong(any(), any()) } returns 30 //saved timestamp

        assertTrue(cacheSession.isTimedOutFor(feature = feature))

        verify {
            preferences.getLong("Test", 0L)
        }
    }
}