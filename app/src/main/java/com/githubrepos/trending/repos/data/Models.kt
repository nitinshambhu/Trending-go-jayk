package com.githubrepos.trending.repos.data

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.githubrepos.trending.common.util.visibilityIf

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
    @get: Bindable var showShimmerEffectVisibility: Int = View.VISIBLE,
    @get: Bindable var listVisibility: Int = View.GONE,
    @get: Bindable var errorStateVisibility: Int = View.GONE
) : BaseObservable() {

    var showErrorState: Boolean = false
        set(value) {
            field = value
            showShimmerEffectVisibility = visibilityIf(visible = !value && !showList)
            errorStateVisibility = visibilityIf(visible = value)
            notifyPropertyChanged(BR.showShimmerEffectVisibility)
            notifyPropertyChanged(BR.errorStateVisibility)
        }

    var showList: Boolean = false
        set(value) {
            field = value
            showShimmerEffectVisibility = visibilityIf(visible = !value && !showErrorState)
            listVisibility = visibilityIf(visible = value)
            notifyPropertyChanged(BR.showShimmerEffectVisibility)
            notifyPropertyChanged(BR.listVisibility)
        }

    fun asMap(): HashMap<String, Boolean> {
        return hashMapOf(
            "showList" to showList,
            "showErrorState" to showErrorState
        )
    }

    fun fromMap(map : Map<String, Boolean>) {
        showList = map["showList"] as Boolean
        showErrorState = map["showErrorState"] as Boolean
    }
}