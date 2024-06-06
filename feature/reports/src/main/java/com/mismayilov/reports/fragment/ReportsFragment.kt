package com.mismayilov.reports.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mismayilov.common.extensions.toCurrencyString
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.reports.R
import com.mismayilov.reports.adapter.ReportAccountListRecyclerAdapter
import com.mismayilov.reports.databinding.FragmentReportsBinding
import com.mismayilov.reports.flow.ReportsEffect
import com.mismayilov.reports.flow.ReportsEvent
import com.mismayilov.reports.flow.ReportsState
import com.mismayilov.reports.viewmodel.ReportsViewModel
import com.mismayilov.uikit.adapter.AccountListRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsFragment : BaseFragment<FragmentReportsBinding,ReportsViewModel,ReportsState,ReportsEvent,ReportsEffect>() {
    override fun getViewModelClass(): Class<ReportsViewModel> = ReportsViewModel::class.java

    private val adapter = ReportAccountListRecyclerAdapter()
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReportsBinding = {
        inflater, container, attachToRoot -> FragmentReportsBinding.inflate(inflater, container, attachToRoot)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireContext(),1)
            recyclerView.adapter = adapter
        }
    }

    @SuppressLint("SetTextI18n")
    override fun renderState(state: ReportsState) {
        binding.apply {
            reportExpense.text = "${state.expenseAmount.toCurrencyString()} $"
            reportIncome.text = "${state.incomeAmount.toCurrencyString()} $"
            reportTransaction.text = "${state.transactionAmount.toCurrencyString()} $"
            totalBalance.text = "${(state.totalAmount).toCurrencyString()} $"
        }
        state.accounts?.let { adapter.submitList(it) }
    }
}