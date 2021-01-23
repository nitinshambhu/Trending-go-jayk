package com.githubrepos.trending.repos.ui.hilt

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.githubrepos.trending.common.BaseViewModel
import com.githubrepos.trending.common.DEBUG_TAG
import com.githubrepos.trending.common.data.LoadingState
import com.githubrepos.trending.common.util.logD
import com.githubrepos.trending.repos.data.RepositoriesUiState
import com.githubrepos.trending.repos.data.repository.RepositoriesRepositoryHilt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class RepositoriesViewModel @ViewModelInject constructor(
    val repo: RepositoriesRepositoryHilt
) : BaseViewModel() {

    private val _loadingStateMutableLiveData = MutableLiveData<LoadingState>()
    val loadingStateLiveData: LiveData<LoadingState> = _loadingStateMutableLiveData

    val uiState = RepositoriesUiState()

    init {
        "VIEW MODEL init ....".logD(DEBUG_TAG)
    }

    fun observeRepositoriesFromDatabase() = repo.fetchFromDatabase()

    fun setLoadingState(state: LoadingState) {
        if (state is LoadingState.InProgress) {
            uiState.apply {
                showErrorState = false
                showList = false
            }
        }
        _loadingStateMutableLiveData.value = state
    }

    fun upDateRepositoriesList() {
        "RepositoriesViewModel :: upDateRepositoriesList :: hashcode ==> ${hashCode()}".logD(DEBUG_TAG)
        "upDateRepositoriesList :: UiState current state : $uiState".logD(DEBUG_TAG)
        viewModelScope.launch(Dispatchers.Main) {
            setLoadingState(LoadingState.InProgress)
            try {
                repo.fetchFromRemote()
                uiState.apply {
                    showErrorState = false
                    showList = true
                }
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                uiState.apply {
                    showErrorState = true
                    showList = false
                }
            } finally {
                setLoadingState(LoadingState.Done)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        "RepositoriesViewModel :: onCleared :: hashcode ==> ${hashCode()}".logD(DEBUG_TAG)
        "UiState current state : $uiState".logD(DEBUG_TAG)
    }
}