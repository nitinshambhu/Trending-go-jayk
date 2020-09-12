package com.githubrepos.trending.repos.ui

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import com.githubrepos.trending.R
import com.githubrepos.trending.common.BaseFragment
import com.githubrepos.trending.common.LoadingState
import com.githubrepos.trending.databinding.FragmentRepositoriesBinding
import com.githubrepos.trending.repos.di.repositoriesModule
import kotlinx.android.synthetic.main.fragment_repositories.*
import kotlinx.android.synthetic.main.layout_shimmer_place_holder_list.view.*
import org.koin.android.scope.currentScope
import org.koin.core.module.Module

class RepositoriesFragment : BaseFragment() {

    lateinit var binding: FragmentRepositoriesBinding

    val viewModel: RepositoriesViewModel by lazy {
        requireActivity().currentScope.get<RepositoriesViewModel>()
    }

    val repositoriesListAdapter: RepositoriesAdapter by lazy {
        requireActivity().currentScope.get<RepositoriesAdapter>()
    }

    override val modulesToLoad: List<Module> = listOf(repositoriesModule)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepositoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reposList.setAdapter(repositoriesListAdapter)
        initListeners()
        viewModel.getList()
    }

    fun initListeners() {

        addDisposable(
            repositoriesListAdapter.observeCollapseGroupEvents().subscribe { groupPosition ->
                reposList.collapseGroup(groupPosition)
            }
        )

        viewModel.repositoriesLiveData.observe(viewLifecycleOwner, Observer {
            swipeToRefresh.isRefreshing = false
            repositoriesListAdapter.setResultList(it)
        })

        viewModel.loadingStateLiveData.observe(viewLifecycleOwner, Observer { loadingState ->
            when (loadingState) {
                is LoadingState.InProgress -> shimmer.shimmerFrameLayout.startShimmerAnimation()
                is LoadingState.Done -> shimmer.shimmerFrameLayout.stopShimmerAnimation()
            }
        })

        swipeToRefresh.setOnRefreshListener {
            viewModel.refresh()
            swipeToRefresh.isRefreshing = true
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_repos, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refresh) {
            viewModel.refresh()
        }
        return super.onOptionsItemSelected(item)
    }

}