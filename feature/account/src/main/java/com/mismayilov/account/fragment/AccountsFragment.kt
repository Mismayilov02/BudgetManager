package com.mismayilov.account.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mismayilov.account.databinding.FragmentAccountsBinding
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.account.adapter.AccountListAdapter
import com.mismayilov.settings.flow.accounts.AccountsState
import com.mismayilov.account.flow.accounts.AccountsEffect
import com.mismayilov.account.flow.accounts.AccountsEvent
import com.mismayilov.account.viewmodel.AccountsViewModel
import com.mismayilov.common.extensions.showDialog
import com.mismayilov.common.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountsFragment :
    BaseFragment<FragmentAccountsBinding, AccountsViewModel, AccountsState, AccountsEvent, AccountsEffect>() {
    override fun getViewModelClass(): Class<AccountsViewModel> = AccountsViewModel::class.java
    private lateinit var accountsAdapter: AccountListAdapter
    private val accounts = mutableListOf<AccountModel>()
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAccountsBinding =
        { inflater, container, attachToRoot ->
            FragmentAccountsBinding.inflate(inflater, container, attachToRoot)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NavigationManager).bottomNavigationVisibility(false)

        initClickListeners()
        initAdapter()
        initRecyclerView()
    }

    private fun initAdapter() {
        accountsAdapter = AccountListAdapter(onEditClick = {id ->
            val directions =
                AccountsFragmentDirections.actionSettingsFragmentToCreateAccountFragment(id)
            (activity as NavigationManager).navigateByDirection(directions)
        }, onDeleteClick = {
          showDialog (positiveButton = {
              setEvent(AccountsEvent.DeleteAccount(it))
          })
        }, onViewClick = { id ->
            val directions =
                AccountsFragmentDirections.actionSettingsFragmentToAccountManagerFragment(id)
            (activity as NavigationManager).navigateByDirection(directions)
        })
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(context, 1)
            recyclerView.adapter = accountsAdapter
        }
    }

    private fun initClickListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                (activity as NavigationManager).back()
                (activity as NavigationManager).bottomNavigationVisibility(true)
            }
        }
    }


    override fun renderState(state: AccountsState) {
        state.accounts?.let {
            accounts.clear()
            accounts.addAll(it)
            accountsAdapter.submitList(accounts)

        }
    }

    override fun renderEffect(effect: AccountsEffect) {
        when (effect) {
            is AccountsEffect.ShowError -> {
                showToast(effect.message)
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        (activity as NavigationManager).bottomNavigationVisibility(true)
    }
}