package com.githubrepos.trending.common.util

import android.view.View
import org.junit.Assert.assertEquals
import org.junit.Test

class AndroidUtilMethodsTest {

    @Test
    fun `Passing visible as true should return View VISIBLE`() {
        val result = visibilityIf(visible = true)
        assertEquals(View.VISIBLE, result)
    }

    @Test
    fun `Passing visible as false should return View GONE`() {
        val result = visibilityIf(visible = false)
        assertEquals(View.GONE, result)
    }
}