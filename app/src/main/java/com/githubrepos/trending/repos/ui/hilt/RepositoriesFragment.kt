package com.githubrepos.trending.repos.ui.hilt

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.githubrepos.trending.R
import com.githubrepos.trending.common.DEBUG_TAG
import com.githubrepos.trending.common.data.LoadingState
import com.githubrepos.trending.common.fragment.BaseFragment
import com.githubrepos.trending.common.util.logD
import com.githubrepos.trending.databinding.FragmentRepositoriesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_repositories.*
import kotlinx.android.synthetic.main.layout_no_internet_connection_error_state.*
import javax.inject.Inject


/**
 *  Displays list of top trending Repositoires on github
 */
@AndroidEntryPoint
class RepositoriesFragment : BaseFragment() {

    lateinit var binding: FragmentRepositoriesBinding

    val viewModel: RepositoriesViewModel by viewModels()

    @Inject
    lateinit var repositoriesListAdapter: RepositoriesAdapter

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
        "onViewCreated ...... ".logD(DEBUG_TAG)
        binding.uiState = viewModel.uiState
        reposList.setAdapter(repositoriesListAdapter)
        initListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        "onActivityCreated ...... test value = ${savedInstanceState?.get("test")} ".logD(DEBUG_TAG)
        if (savedInstanceState == null) {
            viewModel.upDateRepositoriesList()
        }
    }

    fun initListeners() {

        addDisposable(
            repositoriesListAdapter.observeCollapseGroupEvents().subscribe { groupPosition ->
                reposList.collapseGroup(groupPosition)
            }
        )

        viewModel.observeRepositoriesFromDatabase().observe(viewLifecycleOwner, Observer {
            "observeRepositoriesFromDatabase()....".logD(DEBUG_TAG)
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
        outState.putString("test", "Saved!!")
        "RepositoriesFragment :: onSaveInstanceState ==> $outState".logD(DEBUG_TAG)
    }

}