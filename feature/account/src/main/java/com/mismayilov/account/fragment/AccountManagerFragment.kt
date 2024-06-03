package com.mismayilov.account.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mismayilov.account.databinding.FragmentAccountDetailsBinding
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.account.flow.create_account.CreateAccountEffect
import com.mismayilov.account.flow.create_account.CreateAccountEvent
import com.mismayilov.account.flow.create_account.CreateAccountState
import com.mismayilov.account.viewmodel.CreateAccountViewModel
import com.mismayilov.core.managers.NavigationManager

class AccountManagerFragment : BaseFragment<FragmentAccountDetailsBinding, CreateAccountViewModel, CreateAccountState, CreateAccountEvent, CreateAccountEffect>() {
    override fun getViewModelClass(): Class<CreateAccountViewModel> = CreateAccountViewModel::class.java
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