package com.githubrepos.trending.repos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.githubrepos.trending.common.DataFetchType
import com.githubrepos.trending.repos.data.Repository
import com.githubrepos.trending.repos.data.repository.RepositoriesRepository

class RepositoriesViewModel(val repo: RepositoriesRepository) : ViewModel() {

    private val repositoriesMutableLiveData = MutableLiveData<List<Repository>>()
    val repositoriesLiveData: LiveData<List<Repository>> = repositoriesMutableLiveData

    fun getList()  = fetchData()

    fun fetchData(fetchType: DataFetchType = DataFetchType.Normal) {

    }

    fun refresh()  = fetchData(fetchType = DataFetchType.Force)
}