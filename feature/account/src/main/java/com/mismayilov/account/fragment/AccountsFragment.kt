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
import com.mismayilov.uikit.util.showDialog
import com.mismayilov.uikit.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountsFragment :
    BaseFragment<FragmentAccountsBinding, AccountsViewModel, AccountsState, AccountsEvent, AccountsEffect>() {
    override fun getViewModelClass(): Class<AccountsViewModel> = AccountsViewModel::class.java
    private lateinit var accountsAdapter: AccountListAdapter
    private var accounts = mutableListOf<AccountModel>()
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
        accountsAdapter = AccountListAdapter(
            onEditClick = { id ->
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
        },
            onPinClick = {
                setEvent(AccountsEvent.PinAccount(it))
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
           /* customTopBar.backClickListener= {
                (activity as NavigationManager).back()
                (activity as NavigationManager).bottomNavigationVisibility(true)
            }*/
            btnSave.setOnClickListener {
                (activity as NavigationManager).navigateByNavigationName("createAccountFragment")
            }
        }
    }


    override fun renderState(state: AccountsState) {
        state.accounts?.let {
            accounts = it.toMutableList()
            accountsAdapter.submitList(it)

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