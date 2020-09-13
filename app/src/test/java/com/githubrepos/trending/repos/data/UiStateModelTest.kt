package com.githubrepos.trending.repos.data

import android.view.View
import androidx.databinding.library.baseAdapters.BR
import io.mockk.*
import org.junit.Assert.*
import org.junit.Test

class UiStateModelTest {

    fun getState(): RepositoriesUiState = spyk<RepositoriesUiState>().apply {
        every { notifyPropertyChanged(any()) } just Runs
    }

    @Test
    fun `Initial state of UI should display shimmer effect and hide list and error layouts`() {
        val uiState = getState()

        assertEquals(uiState.showShimmerEffectVisibility, View.VISIBLE)
        assertEquals(uiState.errorStateVisibility, View.GONE)
        assertEquals(uiState.listVisibility, View.GONE)

        assertFalse(uiState.showErrorState)
        assertFalse(uiState.showList)
    }

    @Test
    fun `When error is occurred while displaying shimmering effect, error state becomes visible and list, shimmer layouts are gone`() {
        val uiState = getState()

        uiState.showErrorState = true

        assertEquals(uiState.errorStateVisibility, View.VISIBLE)
        assertTrue(uiState.showErrorState)

        assertEquals(uiState.showShimmerEffectVisibility, View.GONE)
        assertEquals(uiState.listVisibility, View.GONE)

        assertFalse(uiState.showList)

        verify {
            uiState.notifyPropertyChanged(BR.showShimmerEffectVisibility)
            uiState.notifyPropertyChanged(BR.errorStateVisibility)
        }
    }

    @Test
    fun `When data is received while displaying shimmering effect, list becomes visible and error, shimmer layouts are gone`() {
        val uiState = getState()

        uiState.showList = true

        assertEquals(uiState.listVisibility, View.VISIBLE)
        assertTrue(uiState.showList)

        assertEquals(uiState.showShimmerEffectVisibility, View.GONE)
        assertEquals(uiState.errorStateVisibility, View.GONE)

        assertFalse(uiState.showErrorState)

        verify {
            uiState.notifyPropertyChanged(BR.showShimmerEffectVisibility)
            uiState.notifyPropertyChanged(BR.listVisibility)
        }
    }

}