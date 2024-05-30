package com.mismayilov.account.viewmodel

import androidx.lifecycle.viewModelScope
import com.mismayilov.domain.usecases.account.GetAllAccountUseCase
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.settings.flow.accounts.AccountsState
import com.mismayilov.account.flow.accounts.AccountsEffect
import com.mismayilov.account.flow.accounts.AccountsEvent
import com.mismayilov.account.fragment.CreateAccountFragmentArgs
import com.mismayilov.domain.usecases.account.CreateAccountUseCase
import com.mismayilov.domain.usecases.account.DeleteAccountUseCase
import com.mismayilov.domain.usecases.account.UpdateAccountPinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val getAllAccountUseCase: GetAllAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val updateAccountPinUseCase: UpdateAccountPinUseCase
) :
    BaseViewModel<AccountsState, AccountsEvent, AccountsEffect>() {
    override fun getInitialState(): AccountsState = AccountsState()

    private val _accounts = MutableStateFlow<List<AccountModel>>(emptyList())
    val accounts: Flow<List<AccountModel>> = _accounts

    init {
        getAllAccount()
    }

    private fun getAllAccount() {
        getAllAccountUseCase.invoke().onEach {
            _accounts.value = it
            setState(getCurrentState().copy(accounts = it))
        }.launchIn(viewModelScope)
    }

    override fun onEventChanged(event: AccountsEvent) {
        when (event) {
            is AccountsEvent.DeleteAccount -> deleteAccount(event.id)
            is AccountsEvent.PinAccount -> pinAccount(event.id)
        }
    }

    private fun pinAccount(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            updateAccountPinUseCase.invoke(id)
//            setState(getCurrentState().copy(accounts = _accounts.value))
        }
    }

    private fun deleteAccount(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val deleteAccount = _accounts.value.filter { it.id == id }[0]
            if (deleteAccount.isPinned) {
                setEffect(AccountsEffect.ShowError("You can't delete pinned account"))
            } else {
                deleteAccountUseCase.invoke(id)
                _accounts.value = _accounts.value.filter { it.id != id }
                setState(getCurrentState().copy(accounts = _accounts.value))
            }
        }

    }

}