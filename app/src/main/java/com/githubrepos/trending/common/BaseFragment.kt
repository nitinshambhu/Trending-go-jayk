package com.githubrepos.trending.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.githubrepos.trending.util.logD
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

abstract class BaseFragment : Fragment() {

    val compositeSubscription = CompositeDisposable()

    open val modulesToLoad: List<Module> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "onCreate called ...".logD(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        "onCreateView called ...".logD(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(modulesToLoad)
        "onViewCreated called ...".logD(this)
    }

    override fun onStart() {
        super.onStart()
        "onStart called ...".logD(this)
    }

    override fun onResume() {
        super.onResume()
        "onResume called ...".logD(this)
    }

    override fun onPause() {
        super.onPause()
        "onPause called ...".logD(this)
    }

    override fun onStop() {
        super.onStop()
        "onStop called ...".logD(this)
    }

    override fun onDestroyView() {
        unloadKoinModules(modulesToLoad)
        super.onDestroyView()
        "onDestroyView called ...".logD(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy called ...".logD(this)
    }

    fun addDisposable(disposable: Disposable) {
        compositeSubscription.add(disposable)
    }

}