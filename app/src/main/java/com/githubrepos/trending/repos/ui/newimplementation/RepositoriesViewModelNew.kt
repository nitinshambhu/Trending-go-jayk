package com.githubrepos.trending.repos.ui.newimplementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.githubrepos.trending.common.BaseViewModel
import com.githubrepos.trending.common.DEBUG_TAG
import com.githubrepos.trending.common.data.LoadingState
import com.githubrepos.trending.common.util.logD
import com.githubrepos.trending.repos.data.RepositoriesUiState
import com.githubrepos.trending.repos.data.repository.RepositoriesRepositoryNew
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class RepositoriesViewModelNew(val repo: RepositoriesRepositoryNew) : BaseViewModel() {

    private val _loadingStateMutableLiveData = MutableLiveData<LoadingState>()
    val loadingStateLiveData: LiveData<LoadingState> = _loadingStateMutableLiveData

    val uiState = RepositoriesUiState()

    fun restoreUIWith(map: Map<String, Boolean>?) {
        "Before restoring uistate = $uiState".logD(DEBUG_TAG)
        map?.apply {
            uiState.fromMap(map)
        }
        "After restoring uistate = $uiState".logD(DEBUG_TAG)
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
        "onCleared() =....".logD(DEBUG_TAG)
    }
}