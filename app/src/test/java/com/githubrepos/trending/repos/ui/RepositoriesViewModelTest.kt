package com.githubrepos.trending.repos.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.githubrepos.trending.common.CoroutineTestRule
import com.githubrepos.trending.common.data.ApiResponse
import com.githubrepos.trending.common.data.DataFetchType
import com.githubrepos.trending.common.data.LoadingState
import com.githubrepos.trending.common.util.asError
import com.githubrepos.trending.common.util.asSuccess
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.repository.RepositoriesRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoriesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    val repo: RepositoriesRepository = mockk()
    val viewModel: RepositoriesViewModel = spyk(RepositoriesViewModel(repo))

    @After
    fun tearDown() {
        unmockkObject(repo, viewModel)
    }

    @Test
    fun `Calling getList method should call fetchData method with fetchType Normal`() {

        every { viewModel.fetchData(any()) } just Runs

        viewModel.getList()

        verify(exactly = 1) {
            viewModel.fetchData(DataFetchType.Normal)
        }
    }

    @Test
    fun `Calling refresh method should always fetchData method with fetchType Force`() {

        every { viewModel.fetchData(any()) } just Runs

        viewModel.refresh()

        verify(exactly = 1) {
            viewModel.fetchData(DataFetchType.Force)
        }
    }

    @Test
    fun `Handle success when calling fetchData with Normal fetch type`() {
        val list = listOf<Repository>()
        val apiResponse = list.asSuccess()
        coEvery { repo.getRepositories(any()) } returns apiResponse

        runBlockingTest {

            viewModel.fetchData()

            assertEquals(LoadingState.Done, viewModel.loadingStateLiveData.value)
            assertTrue(viewModel.uiState.showList)
            assertFalse(viewModel.uiState.showErrorState)

            coVerifyOrder {
                viewModel.fetchData(DataFetchType.Normal)
                viewModel.setLoadingState(LoadingState.InProgress)
                repo.getRepositories(DataFetchType.Normal)
                viewModel.handleSuccess((apiResponse as ApiResponse.Success).data)
                viewModel.setLoadingState(LoadingState.Done)
            }
            coVerify(exactly = 0) { viewModel.handleError() }
        }
    }

    @Test
    fun `Handle Error when calling fetchData with Normal fetch type`() {
        coEvery { repo.getRepositories(any()) } returns RuntimeException("Test exception").asError()

        runBlockingTest {

            viewModel.fetchData()

            assertEquals(LoadingState.Done, viewModel.loadingStateLiveData.value)
            assertTrue(viewModel.uiState.showErrorState)
            assertFalse(viewModel.uiState.showList)

            coVerifyOrder {
                viewModel.fetchData(DataFetchType.Normal)
                viewModel.setLoadingState(LoadingState.InProgress)
                repo.getRepositories(DataFetchType.Normal)
                viewModel.handleError()
                viewModel.setLoadingState(LoadingState.Done)
            }
            coVerify(exactly = 0) { viewModel.handleSuccess(any()) }
        }
    }


    @Test
    fun `Handle success when calling fetchData with Force fetch type`() {
        val list = listOf<Repository>()
        val apiResponse = list.asSuccess()
        coEvery { repo.getRepositories(any()) } returns apiResponse

        runBlockingTest {

            viewModel.fetchData(fetchType = DataFetchType.Force)

            assertEquals(LoadingState.Done, viewModel.loadingStateLiveData.value)
            assertTrue(viewModel.uiState.showList)
            assertFalse(viewModel.uiState.showErrorState)

            coVerifyOrder {
                viewModel.fetchData(DataFetchType.Force)
                viewModel.setLoadingState(LoadingState.InProgress)
                repo.getRepositories(DataFetchType.Force)
                viewModel.handleSuccess((apiResponse as ApiResponse.Success).data)
                viewModel.setLoadingState(LoadingState.Done)
            }
            coVerify(exactly = 0) { viewModel.handleError() }
        }
    }

    @Test
    fun `Handle Error when calling fetchData with Force fetch type`() {
        coEvery { repo.getRepositories(any()) } returns RuntimeException("Test exception").asError()

        runBlockingTest {

            viewModel.fetchData(fetchType = DataFetchType.Force)

            assertEquals(LoadingState.Done, viewModel.loadingStateLiveData.value)
            assertTrue(viewModel.uiState.showErrorState)
            assertFalse(viewModel.uiState.showList)

            coVerifyOrder {
                viewModel.fetchData(DataFetchType.Force)
                viewModel.setLoadingState(LoadingState.InProgress)
                repo.getRepositories(DataFetchType.Force)
                viewModel.handleError()
                viewModel.setLoadingState(LoadingState.Done)
            }
            coVerify(exactly = 0) { viewModel.handleSuccess(any()) }
        }
    }

    @Test
    fun `Method handleError should set the error state on UI state to true leaving showList set to false`() {
        assertFalse(viewModel.uiState.showErrorState)
        assertFalse(viewModel.uiState.showList)

        viewModel.handleError()

        assertTrue(viewModel.uiState.showErrorState)
        assertFalse(viewModel.uiState.showList)
        assertEquals(LoadingState.Done, viewModel.loadingStateLiveData.value)

        verify(exactly = 1) { viewModel.setLoadingState(LoadingState.Done) }
    }

    @Test
    fun `Method handleSuccess should set the show List on UI state to true leaving showErrorState set to false`() {

        assertFalse(viewModel.uiState.showErrorState)
        assertFalse(viewModel.uiState.showList)

        viewModel.handleSuccess(listOf())

        assertTrue(viewModel.uiState.showList)
        assertFalse(viewModel.uiState.showErrorState)
        assertEquals(LoadingState.Done, viewModel.loadingStateLiveData.value)

        verify(exactly = 1) { viewModel.setLoadingState(LoadingState.Done) }
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
}