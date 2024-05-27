package com.mismayilov.account.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mismayilov.account.databinding.FragmentAccountDetailsBinding
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.settings.flow.create_account.AccountDetailsEffect
import com.mismayilov.settings.flow.create_account.AccountDetailsEvent
import com.mismayilov.settings.flow.create_account.AccountDetailsState
import com.mismayilov.account.viewmodel.AccountDetailsViewModel
import com.mismayilov.core.managers.NavigationManager

class AccountDetailsFragment : BaseFragment<FragmentAccountDetailsBinding, AccountDetailsViewModel, AccountDetailsState, AccountDetailsEvent, AccountDetailsEffect>() {
    override fun getViewModelClass(): Class<AccountDetailsViewModel> = AccountDetailsViewModel::class.java
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAccountDetailsBinding =
        { inflater, container, attachToRoot ->
            FragmentAccountDetailsBinding.inflate(inflater, container, attachToRoot)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initClickListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                (activity as NavigationManager).back()
            }
        }
    }

}