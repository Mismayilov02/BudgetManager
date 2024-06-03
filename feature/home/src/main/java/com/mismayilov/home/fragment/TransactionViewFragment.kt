package com.mismayilov.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.mismayilov.common.utility.Util.Companion.DATE_FORMAT
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.home.databinding.FragmentViewTransactionBinding
import com.mismayilov.home.flow.transaction_view.TransactionViewEffect
import com.mismayilov.home.flow.transaction_view.TransactionViewEvent
import com.mismayilov.home.flow.transaction_view.TransactionViewState
import com.mismayilov.home.viewmodel.TransactionViewViewModel
import com.mismayilov.uikit.util.getResourceIdByName
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class TransactionViewFragment :BaseFragment<FragmentViewTransactionBinding,TransactionViewViewModel, TransactionViewState, TransactionViewEvent, TransactionViewEffect>() {

    override fun getViewModelClass(): Class<TransactionViewViewModel> =
        TransactionViewViewModel::class.java

    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentViewTransactionBinding =
        { inflater, container, attachToRoot ->
            FragmentViewTransactionBinding.inflate(inflater, container, attachToRoot)
        }

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
    private val args: TransactionViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NavigationManager)?.bottomNavigationVisibility(false)
        setEvent(TransactionViewEvent.GetTransaction(args.id))
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.customTopBar.backClickListener = {
            (activity as NavigationManager).back()
        }
        binding.customTopBar.rightClickListener = {
            val bundle = Bundle()
            bundle.putLong("id", args.id)
            (activity as NavigationManager).navigateByBundle("create_navigation", bundle)
        }
    }

    override fun renderState(state: TransactionViewState) {
        state.transaction?.let {
            setData(it)
        }
    }

    private fun setData(transaction: TransactionModel) {
        binding.apply {
            iconImage.setImageResource(getResourceIdByName( requireContext(),transaction.category.icon))
            transactionName.text = transaction.category.name
            transactionAmount.text = transaction.amount.toString()
            transactionNote.text = transaction.note
            transactionDate.text = simpleDateFormat.format(transaction.date)
            transactionType.text = if (transaction.category.type == "ACCOUNT") "Transfer" else transaction.category.type.toLowerCase().capitalize()
            transactionAccount.text = transaction.account!!.name + if (transaction.accountTo != null) " -> ${transaction.accountTo?.name}" else ""
        }
    }

}