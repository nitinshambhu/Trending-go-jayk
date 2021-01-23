package com.githubrepos.trending.repos.ui.newimplementation

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import com.githubrepos.trending.R
import com.githubrepos.trending.common.DEBUG_TAG
import com.githubrepos.trending.common.fragment.BaseFragment
import com.githubrepos.trending.common.data.LoadingState
import com.githubrepos.trending.common.util.logD
import com.githubrepos.trending.databinding.FragmentRepositoriesBinding
import com.githubrepos.trending.repos.di.repositoriesModule
import com.githubrepos.trending.repos.ui.koin.RepositoriesAdapter
import kotlinx.android.synthetic.main.fragment_repositories.*
import kotlinx.android.synthetic.main.layout_no_internet_connection_error_state.*
import org.koin.android.scope.currentScope
import org.koin.core.module.Module

/**
 *  Displays list of top trending Repositoires on github
 */
class RepositoriesFragmentNew : BaseFragment() {

    lateinit var binding: FragmentRepositoriesBinding

    val viewModel: RepositoriesViewModelNew by lazy {
        requireActivity().currentScope.get<RepositoriesViewModelNew>()
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
        "onViewCreated .... ".logD(DEBUG_TAG)
        binding.uiState = viewModel.uiState
        reposList.setAdapter(repositoriesListAdapter)
        initListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        "onActivityCreated .... ".logD(DEBUG_TAG)
        savedInstanceState?.run {
            viewModel.restoreUIWith(getSerializable("uiState") as Map<String, Boolean>)
        } ?: viewModel.upDateRepositoriesList()
    }

    fun initListeners() {

        addDisposable(
            repositoriesListAdapter.observeCollapseGroupEvents().subscribe { groupPosition ->
                reposList.collapseGroup(groupPosition)
            }
        )

        viewModel.observeRepositoriesFromDatabase().observe(viewLifecycleOwner, Observer {
            "observeRepositoriesFromDatabase .... setting data".logD(DEBUG_TAG)
            repositoriesListAdapter.setResultList(it)
        })

        viewModel.loadingStateLiveData.observe(viewLifecycleOwner, Observer { loadingState ->
            when (loadingState) {
                is LoadingState.InProgress -> shimmerFrameLayout.startShimmerAnimation()
                is LoadingState.Done -> {
                    swipeToRefresh.isRefreshing = false
                    shimmerFrameLayout.stopShimmerAnimation()
                }
            }
        })

        swipeToRefresh.setOnRefreshListener {
            viewModel.upDateRepositoriesList()
            swipeToRefresh.isRefreshing = true
        }

        retry.setOnClickListener { viewModel.upDateRepositoriesList() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_repos, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refresh) {
            viewModel.upDateRepositoriesList()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        "onSaveInstanceState .... ".logD(DEBUG_TAG)
        outState.putSerializable("uiState", viewModel.uiState.asMap())
    }
}