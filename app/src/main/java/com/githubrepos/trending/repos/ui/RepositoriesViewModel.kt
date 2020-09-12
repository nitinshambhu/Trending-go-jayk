package com.githubrepos.trending.repos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githubrepos.trending.common.DataFetchType
import com.githubrepos.trending.common.LoadingState
import com.githubrepos.trending.repos.data.RepositoriesUiState
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.repository.RepositoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepositoriesViewModel(val repo: RepositoriesRepository) : ViewModel() {

    private val repositoriesMutableLiveData = MutableLiveData<List<Repository>>()
    private val loadingStateMutableLiveData = MutableLiveData<LoadingState>()

    val uiState = RepositoriesUiState()
    val repositoriesLiveData: LiveData<List<Repository>> = repositoriesMutableLiveData
    val loadingStateLiveData: LiveData<LoadingState> = loadingStateMutableLiveData

    fun getList()  = fetchData()

    fun refresh()  = fetchData(fetchType = DataFetchType.Force)

    fun fetchData(fetchType: DataFetchType = DataFetchType.Normal) {
        viewModelScope.launch(Dispatchers.Main) {
            setLoadingState(LoadingState.InProgress)
            val repoList = repo.getRepositories(fetchType = fetchType)
            uiState.apply { showList = true }
            repositoriesMutableLiveData.value = repoList
            setLoadingState(LoadingState.Done)
        }
    }

    fun setLoadingState(state : LoadingState) {
        if (state is LoadingState.InProgress) {
            uiState.apply {
                showErrorState = false
                showList = false
            }
        }
        loadingStateMutableLiveData.value = state
    }
}