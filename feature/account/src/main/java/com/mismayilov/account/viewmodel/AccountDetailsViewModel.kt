package com.mismayilov.account.viewmodel

import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.settings.flow.create_account.AccountDetailsEffect
import com.mismayilov.settings.flow.create_account.AccountDetailsEvent
import com.mismayilov.settings.flow.create_account.AccountDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountDetailsViewModel @Inject constructor():
    BaseViewModel<AccountDetailsState, AccountDetailsEvent, AccountDetailsEffect>() {
    override fun getInitialState(): AccountDetailsState = AccountDetailsState()

    override fun onEventChanged(event: AccountDetailsEvent) {
        /*
                when(event){
                    is AccountManagerEvent.AccountsLoaded -> accountsLoaded(event.accounts)
                }
        */
    }

}