package com.mismayilov.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mismayilov.common.unums.IconType
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.home.adapter.RecentTransactionAdapter
import com.mismayilov.home.databinding.FragmentHomeBinding
import com.mismayilov.home.flow.home.HomeEffect
import com.mismayilov.home.flow.home.HomeEvent
import com.mismayilov.home.flow.home.HomeState
import com.mismayilov.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel, HomeState, HomeEvent, HomeEffect>() {
    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        { inflater, container, attachToRoot ->
            FragmentHomeBinding.inflate(inflater, container, attachToRoot)
        }

    private val adapter = RecentTransactionAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NavigationManager)?.bottomNavigationVisibility(true)

        initClickListeners()
        binding.apply {
            transactionHistoryRecyclerView.setHasFixedSize(true)
            transactionHistoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
            transactionHistoryRecyclerView.adapter = adapter
        }
    }

    private fun initClickListeners() {
        binding.apply {
            val transactionBtn = mutableListOf(btnExpense, btnIncome, btnTransfer)
            transactionBtn.forEach {
                it.setOnClickListener { (activity as NavigationManager).navigateByNavigationName("create_navigation") }
            }
            btnHistory.setOnClickListener {
                (activity as NavigationManager).navigateByNavigationName(
                    "historyFragment"
                )
            }
        }
    }

    override fun renderState(state: HomeState) {
        state.transactionList?.let { adapter.submitList(it) }
    }
}