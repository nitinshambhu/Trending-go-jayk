package com.githubrepos.trending.repos.ui.newimplementation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.githubrepos.trending.common.CoroutineTestRule
import com.githubrepos.trending.common.data.LoadingState
import com.githubrepos.trending.repos.data.repository.RepositoriesRepositoryNew
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class RepositoriesViewModelNewTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    val repo: RepositoriesRepositoryNew = mockk(relaxUnitFun = true)
    val viewModel: RepositoriesViewModelNew = spyk(RepositoriesViewModelNew(repo))

    @After
    fun tearDown() {
        unmockkObject(repo, viewModel)
    }

    @Test
    fun `Method setLoading should set the showList and showErrorState to false if loading state is InProgress`() {

        /**
         *  Setting both showList and showErrorState to true - which should never be the case
         *  Now, setLoading() should successfully set both these booleans to false ONLY if the state is InProgress
         */
        viewModel.uiState.apply {
            showErrorState = true
            showList = true
        }
        assertTrue(viewModel.uiState.showErrorState)
        assertTrue(viewModel.uiState.showList)

        viewModel.setLoadingState(LoadingState.InProgress)

        assertFalse(viewModel.uiState.showList)
        assertFalse(viewModel.uiState.showErrorState)
        assertEquals(LoadingState.InProgress, viewModel.loadingStateLiveData.value)
    }

    @Test
    fun `Method setLoading should leave showList and showErrorState values untouched if loading state is Done`() {

        /**
         *  Setting both showList and showErrorState to true - which should never be the case
         *  Now, setLoading() should NOT change these booleans ONLY if the state is NOT InProgress
         */
        viewModel.uiState.apply {
            showErrorState = true
            showList = true
        }
        assertTrue(viewModel.uiState.showErrorState)
        assertTrue(viewModel.uiState.showList)

        viewModel.setLoadingState(LoadingState.Done)

        // Both the booleans should remain the same as those before we call setLoadingMethod(Done)
        assertTrue(viewModel.uiState.showErrorState)
        assertTrue(viewModel.uiState.showList)
        assertEquals(LoadingState.Done, viewModel.loadingStateLiveData.value)
    }


    @Test
    fun `Method upDateRepositoriesList should set the showList to true and showErrorState to false if remote fetch is successful`() {

        runBlockingTest {

            every { viewModel.setLoadingState(any()) } just Runs

            viewModel.upDateRepositoriesList()

            assertTrue(viewModel.uiState.showList)
            assertFalse(viewModel.uiState.showErrorState)

            coVerifyOrder {
                viewModel.setLoadingState(LoadingState.InProgress)
                repo.fetchFromRemote()
                viewModel.setLoadingState(LoadingState.Done)
            }
        }
    }

    @Test
    fun `Method upDateRepositoriesList should set the showList to false and showErrorState to true if remote fetch throws an UnknownHostException`() {

        runBlockingTest {

            every { viewModel.setLoadingState(any()) } just Runs
            coEvery { repo.fetchFromRemote() } throws UnknownHostException("Test message")

            viewModel.upDateRepositoriesList()

            assertTrue(viewModel.uiState.showErrorState)
            assertFalse(viewModel.uiState.showList)

            coVerifyOrder {
                viewModel.setLoadingState(LoadingState.InProgress)
                repo.fetchFromRemote()
                viewModel.setLoadingState(LoadingState.Done)
            }
        }
    }
}