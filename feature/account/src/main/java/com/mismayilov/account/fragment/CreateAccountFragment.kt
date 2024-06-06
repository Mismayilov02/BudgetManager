package com.mismayilov.account.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.mismayilov.account.databinding.FragmentCreateAccountBinding
import com.mismayilov.account.viewmodel.CreateAccountViewModel
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.account.flow.create_account.CreateAccountEffect
import com.mismayilov.account.flow.create_account.CreateAccountEvent
import com.mismayilov.account.flow.create_account.CreateAccountState
import com.mismayilov.common.unums.CurrencyType
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.uikit.adapter.IconListRecyclerAdapter
import com.mismayilov.uikit.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment :
    BaseFragment<FragmentCreateAccountBinding, CreateAccountViewModel, CreateAccountState, CreateAccountEvent, CreateAccountEffect>() {
    override fun getViewModelClass(): Class<CreateAccountViewModel> =
        CreateAccountViewModel::class.java

    private val args: CreateAccountFragmentArgs by navArgs()
    private val adapter = IconListRecyclerAdapter(false)

    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateAccountBinding =
        { inflater, container, attachToRoot ->
            FragmentCreateAccountBinding.inflate(inflater, container, attachToRoot)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        initRecyclerView()
        checkIsEdit()
    }

    private fun checkIsEdit() {
        setEvent(CreateAccountEvent.GetAccount(args.id))
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
            recyclerView.adapter = adapter
        }
    }


    private fun initClickListeners() {
        binding.apply {
            customTopBar.rightClickListener = {
                setEvent(
                    CreateAccountEvent.CreateAccount(
                        accountName = accountNameTxt.text.toString(),
                        accountType = accountTypeSpinner.spinnerText,
                        iconPosition = adapter.selectedPosition,
                        currency = currencySpinner.spinnerText,
                        balance = accountAmountTxt.text.toString(),
                        isUpdate = args.id != -1L
                    )
                )
            }
            customTopBar.setOnClickListener {
                (activity as NavigationManager).back()
            }
        }
    }

    override fun renderEffect(effect: CreateAccountEffect) {
        when (effect) {
            is CreateAccountEffect.ShowToast -> showToast(effect.message)
            is CreateAccountEffect.CloseFragment -> (activity as NavigationManager).back()
        }
    }

    override fun renderState(state: CreateAccountState) {
        state.apply {
            icons?.let { adapter.submitList(it) }
            accountTypeList?.let { binding.accountTypeSpinner.setSpinnerData(it) }
            currencyData?.let { binding.currencySpinner.setSpinnerData(it) }
            account?.let { setAccountData(it) }
        }

    }

    private fun setAccountData(it: AccountModel) {
        binding.apply {
            currencySpinner.setSpinnerEnabled(false)
            accountNameTxt.setText(it.name)
            accountTypeSpinner.setSelection(it.type)
            currencySpinner.setSelection(CurrencyType.valueOf(it.currency).value)
            accountAmountTxt.setText(it.amount.toString())
            adapter.setSelectedPosition(it.icon)
        }
    }
}