package com.githubrepos.trending.repos.ui.hilt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.githubrepos.trending.databinding.LayoutReposListChildItemBinding
import com.githubrepos.trending.databinding.LayoutReposListItemBinding
import com.githubrepos.trending.repos.data.Repository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class RepositoriesAdapter @Inject constructor(@ApplicationContext context: Context) : BaseExpandableListAdapter() {

    private val CHILD_COUNT = 1
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val collapseGroupEvent = PublishSubject.create<Int>()

    private var repositoryList: MutableList<Repository> = mutableListOf()
    private var lastExpandedGroupPosition = -1

    fun observeCollapseGroupEvents(): Observable<Int> = collapseGroupEvent

    fun setResultList(repoList: List<Repository>) {
        if (groupCount != 0) {
            repositoryList.clear()
        }
        repositoryList.addAll(repoList)
        notifyDataSetChanged()
    }

    fun getRepository(index: Int): Repository {
        return repositoryList[index]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Repository {
        return getRepository(groupPosition)
    }

    override fun getGroup(groupPosition: Int): Repository {
        return getRepository(groupPosition)
    }

    override fun getChildrenCount(groupPosition: Int): Int = CHILD_COUNT

    override fun getGroupCount(): Int {
        return repositoryList.size
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        var childView = convertView
        if (convertView == null) {
            val holder =
                ChildViewHolder(inflater = layoutInflater)
            childView = holder.view().apply { tag = holder }
        }

        val viewHolder = childView!!.tag as ChildViewHolder
        val repository = getChild(groupPosition, childPosition)
        viewHolder.bind(trendingRepository = repository)

        return childView
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var groupView = convertView
        if (convertView == null) {
            val holder =
                GroupViewHolder(inflater = layoutInflater)
            groupView = holder.view().apply { tag = holder }
        }

        val viewHolder = groupView!!.tag as GroupViewHolder
        val repository = getGroup(groupPosition)
        viewHolder.bind(trendingRepository = repository)

        return groupView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun hasStableIds(): Boolean = false

    override fun onGroupExpanded(groupPosition: Int) {
        if (groupPosition != lastExpandedGroupPosition) {
            collapseGroupEvent.onNext(lastExpandedGroupPosition)
        }
        lastExpandedGroupPosition = groupPosition
    }
}

class GroupViewHolder(inflater: LayoutInflater) {
    private val binding = LayoutReposListItemBinding.inflate(inflater)
    fun view(): View = binding.root
    fun bind(trendingRepository: Repository) {
        binding.repository = trendingRepository
    }
}

class ChildViewHolder(inflater: LayoutInflater) {
    private val binding = LayoutReposListChildItemBinding.inflate(inflater)
    fun view(): View = binding.root
    fun bind(trendingRepository: Repository) {
        binding.repository = trendingRepository
    }
}