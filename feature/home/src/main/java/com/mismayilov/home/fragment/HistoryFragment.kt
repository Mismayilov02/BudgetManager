package com.mismayilov.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.textfield.TextInputEditText
import com.mismayilov.common.utility.Util.Companion.DATE_TIME_WITH_TRIM
import com.mismayilov.common.utility.Util.Companion.ONE_WEEK_IN_MILLIS
import com.mismayilov.common.utility.showDatePicker
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.home.adapter.RecentTransactionAdapter
import com.mismayilov.common.generic.SwipeToDeleteCallback
import com.mismayilov.home.databinding.FragmentHistoryBinding
import com.mismayilov.home.flow.history.HistoryEffect
import com.mismayilov.home.flow.history.HistoryEvent
import com.mismayilov.home.flow.history.HistoryState
import com.mismayilov.home.viewmodel.HistoryViewModel
import com.mismayilov.uikit.util.showDialog
import com.mismayilov.uikit.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding,HistoryViewModel,HistoryState,HistoryEvent,HistoryEffect>() {
    override fun getViewModelClass(): Class<HistoryViewModel> = HistoryViewModel::class.java
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding= {
        inflater, container, attachToRoot -> FragmentHistoryBinding.inflate(inflater, container, attachToRoot)
    }
    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat(DATE_TIME_WITH_TRIM)
   private lateinit var adapter: RecentTransactionAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NavigationManager)?.bottomNavigationVisibility(false)
        setupDate()
        setOnClickListeners()
        initAdapter()
        setupRecyclerView()
    }

    private fun initAdapter() {
        adapter = RecentTransactionAdapter(
            deleteItem = {
                showDialog(positiveButton = {
                    viewModel.setEvent(HistoryEvent.DeleteTransaction(it))
                },
                    negativeButton = {
                        adapter.cancelDeleteItem()
                    }
                )
            },
            viewItem = {
                val directions = HistoryFragmentDirections.actionHistoryFragmentToEditTransactionFragment(it)
                (activity as NavigationManager).navigateByDirection(directions)
            }
        )
        val swipeHandler = SwipeToDeleteCallback(requireContext()){
            adapter.deleteItem(it)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.historyRecyclerView)
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnSearch.setOnClickListener {
                viewModel.setEvent(HistoryEvent.GetHistory(
                    simpleDateFormat.parse(historyStartDate.text.toString())!!.time,
                    simpleDateFormat.parse(historyEndDate.text.toString())!!.time
                ))
            }
            historyEndDate.setOnClickListener {
                getDatePicker(historyEndDate)
            }
            historyStartDate.setOnClickListener {
                getDatePicker(historyStartDate)
            }
            customTopBar.backClickListener = {
                (activity as NavigationManager).back()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDatePicker(date: TextInputEditText) {
        showDatePicker(requireContext()) {
            date.setText(SimpleDateFormat(DATE_TIME_WITH_TRIM).format(it))
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            historyRecyclerView.setHasFixedSize(true)
            historyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
            historyRecyclerView.adapter = adapter
        }
    }

    private fun setupDate() {
        binding.apply {
            val startDate:String = simpleDateFormat.format(System.currentTimeMillis() - ONE_WEEK_IN_MILLIS)
            val endDate:String = simpleDateFormat.format(System.currentTimeMillis())
            historyStartDate.setText(startDate)
            historyEndDate.setText(endDate)
        }
    }

    override fun renderEffect(effect: HistoryEffect) {
        when(effect){
            is HistoryEffect.ShowToast -> showToast(effect.message)
        }
    }

    override fun renderState(state: HistoryState) {
        binding.historyNotFoundImage.visibility = if (state.history.isNullOrEmpty()) View.VISIBLE else View.GONE
        adapter.submitList(state.history)
    }


}