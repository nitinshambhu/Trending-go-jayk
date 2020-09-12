package com.githubrepos.trending.repos.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TrendingRepositories")
data class Repository(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val author: String = "",
    val name: String = "",
    val avatar: String = "",
    val description: String = "",
    val language: String? = "",
    val stars: String = "",
    val forks: String = ""
)

data class RepositoriesUiState(
    @get: Bindable var showShimmerEffect: Boolean = true
) : BaseObservable() {

    @get: Bindable
    var showErrorState: Boolean = false
        set(value) {
            field = value
            showShimmerEffect = !field && !showList
            notifyPropertyChanged(BR.showShimmerEffect)
            notifyPropertyChanged(BR.showErrorState)
        }

    @get: Bindable
    var showList: Boolean = false
        set(value) {
            field = value
            showShimmerEffect = !field && !showErrorState
            notifyPropertyChanged(BR.showShimmerEffect)
            notifyPropertyChanged(BR.showList)
        }
}