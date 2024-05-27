package com.mismayilov.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.home.R
import com.mismayilov.home.databinding.FragmentHistoryBinding
import com.mismayilov.home.flow.history.HistoryEffect
import com.mismayilov.home.flow.history.HistoryEvent
import com.mismayilov.home.flow.history.HistoryState
import com.mismayilov.home.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding,HistoryViewModel,HistoryState,HistoryEvent,HistoryEffect>() {
    override fun getViewModelClass(): Class<HistoryViewModel> = HistoryViewModel::class.java
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding= {
        inflater, container, attachToRoot -> FragmentHistoryBinding.inflate(inflater, container, attachToRoot)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NavigationManager)?.bottomNavigationVisibility(false)
    }


}