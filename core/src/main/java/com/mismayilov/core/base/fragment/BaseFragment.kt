package com.mismayilov.core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.mismayilov.core.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel<State, Event, Effect>, State, Event, Effect> :
    Fragment() {
    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    private var _viewModel: VM? = null
    protected val viewModel: VM get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel = ViewModelProvider(this)[getViewModelClass()]
        initStates()
        initActions()
    }

    private fun initActions() {
        viewModel.effect.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { effect -> renderEffect(effect) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initStates() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> renderState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun getViewModelClass(): Class<VM>
    abstract val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB
    protected open fun renderState(state: State) {}
    protected open fun renderEffect(effect: Effect) {}
    protected open fun setEvent(event: Event) {
        viewModel.setEvent(event)
    }
}