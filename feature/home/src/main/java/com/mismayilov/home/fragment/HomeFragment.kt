package com.mismayilov.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.mismayilov.common.extensions.toBlurText
import com.mismayilov.common.extensions.toCurrencyString
import com.mismayilov.common.unums.CurrencyType
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.data.local.SharedPreferencesManager
import com.mismayilov.home.adapter.RecentTransactionAdapter
import com.mismayilov.common.generic.SwipeToDeleteCallback
import com.mismayilov.home.databinding.FragmentHomeBinding
import com.mismayilov.home.flow.home.HomeEffect
import com.mismayilov.home.flow.home.HomeEvent
import com.mismayilov.home.flow.home.HomeState
import com.mismayilov.home.viewmodel.HomeViewModel
import com.mismayilov.uikit.util.showDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel, HomeState, HomeEvent, HomeEffect>() {
    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        { inflater, container, attachToRoot ->
            FragmentHomeBinding.inflate(inflater, container, attachToRoot)
        }

    private lateinit var adapter: RecentTransactionAdapter
    private var showBalance by Delegates.notNull<Boolean>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NavigationManager)?.bottomNavigationVisibility(true)
        checkShowBalance()
        initClickListeners()
        initAdapter()
        initRecyclerView()
    }

    private fun checkShowBalance() {
        binding.apply {
            showBalance = SharedPreferencesManager.getValue("hideBalance", true)
            val textViewList = listOf(amountPinnedAccount, incomeTxt, expenseTxt)
            textViewList.forEach {
                it.toBlurText(showBalance)
        }
        }
    }

    private fun initAdapter() {
        adapter = RecentTransactionAdapter( showBalance,
            deleteItem = {
                showDialog(positiveButton = {
                    setEvent(HomeEvent.DeleteTransaction(it))
                },
                    negativeButton = {
                        adapter.cancelDeleteItem()
                    }
                )
            },
            viewItem = {
                val directions =
                    HomeFragmentDirections.actionHomeFragmentToEditTransactionFragment(it)
                (activity as NavigationManager).navigateByDirection(directions)
            }
        )
        val swipeHandler = SwipeToDeleteCallback(requireContext()) {
            adapter.deleteItem(it)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.transactionHistoryRecyclerView)
    }

    private fun initRecyclerView() {
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
                it.setOnClickListener { _ ->
                    val bundle = Bundle()
                    bundle.putInt("tabSelectedIndex", transactionBtn.indexOf(it))
                    (activity as NavigationManager).navigateByBundle(
                        "create_navigation",
                        bundle
                    )
                }
            }
            btnHistory.setOnClickListener {
                (activity as NavigationManager).navigateByNavigationName(
                    "historyFragment"
                )
            }
        }
    }

    override fun renderState(state: HomeState) {
            binding.historyNotFoundImage.visibility =
                if (state.transactionList.isNullOrEmpty()) View.VISIBLE else View.GONE
            state.transactionList?.let { adapter.submitList(it) }
            state.accountData?.let {
                setAccountData(state)
            }

    }

    private fun setAccountData(homeState: HomeState) {
        binding.apply {
            val accountModel = homeState.accountData!!
            val incomeSum = homeState.incomeSum
            val expenseSum = homeState.expenseSum
            val currencySymbol = CurrencyType.valueOf(accountModel.currency).symbol
            amountPinnedAccount.text = "${accountModel.amount.toCurrencyString()} $currencySymbol"
            incomeTxt.text = "${incomeSum.toCurrencyString()} $currencySymbol"
            expenseTxt.text = "${expenseSum.toCurrencyString()} $currencySymbol"
        }
    }
}