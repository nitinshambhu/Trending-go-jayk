package com.githubrepos.trending.repos.ui.koin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.githubrepos.trending.common.BaseViewModel
import com.githubrepos.trending.common.data.DataFetchType
import com.githubrepos.trending.common.data.LoadingState
import com.githubrepos.trending.common.util.apiResponseHandler
import com.githubrepos.trending.common.util.logD
import com.githubrepos.trending.repos.data.RepositoriesUiState
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.repository.RepositoriesRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepositoriesViewModel(val repo: RepositoriesRepository) : BaseViewModel() {

    private val _repositoriesMutableLiveData = MutableLiveData<List<Repository>>()
    private val _loadingStateMutableLiveData = MutableLiveData<LoadingState>()

    val uiState = RepositoriesUiState()
    val repositoriesLiveData: LiveData<List<Repository>> = _repositoriesMutableLiveData
    val loadingStateLiveData: LiveData<LoadingState> = _loadingStateMutableLiveData

    fun getList() = fetchData()

    fun refresh() = fetchData(fetchType = DataFetchType.Force)

    fun fetchData(fetchType: DataFetchType = DataFetchType.Normal) {

        viewModelScope.launch(Dispatchers.Main) {

            setLoadingState(LoadingState.InProgress)

            apiResponseHandler(
                status = repo.getRepositories(fetchType = fetchType),
                onSuccess = { listOfRepos -> handleSuccess(reposList = listOfRepos) },
                onError = { handleError() }
            )
        }
    }

    fun handleSuccess(reposList : List<Repository>) {
        uiState.apply {
            showList = true
            showErrorState = false
        }
        _repositoriesMutableLiveData.value = reposList
        setLoadingState(LoadingState.Done)
    }

    fun handleError() {
        uiState.apply {
            showErrorState = true
            showList = false
        }
        setLoadingState(LoadingState.Done)
    }

    fun setLoadingState(state: LoadingState) {
        if (state is LoadingState.InProgress) {
            uiState.apply {
                showErrorState = false
                showList = false
            }
        }
        _loadingStateMutableLiveData.value = state
    }

    // Rx demonstration purpose only
    fun fetchDataRx(fetchType: DataFetchType = DataFetchType.Normal) {
        addDisposable(
            repo.getRepositoriesRx(fetchType = fetchType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    "Rx call list fetch success".logD("FromRx===")
                    handleSuccess(it)
                }, {
                    "Error Thrown Rx = ${it.message}".logD("FromRx===")
                    handleError()
                })
        )

    }
}