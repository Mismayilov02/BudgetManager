package com.mismayilov.account.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.mismayilov.account.flow.create_account.CreateAccountState
import com.mismayilov.common.unums.AccountType
import com.mismayilov.common.unums.IconType
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.domain.usecases.icon.GetAllIconUseCase
import com.mismayilov.account.flow.create_account.CreateAccountEffect
import com.mismayilov.account.flow.create_account.CreateAccountEvent
import com.mismayilov.common.unums.CurrencyType
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.usecases.account.CreateAccountUseCase
import com.mismayilov.domain.usecases.account.GetAccountByIDUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val context: Context,
    private val createAccountUseCase: CreateAccountUseCase,
    private val getAccountByIDUseCase: GetAccountByIDUseCase,
) :
    BaseViewModel<CreateAccountState, CreateAccountEvent, CreateAccountEffect>() {
    override fun getInitialState(): CreateAccountState = CreateAccountState()

    private val _icons = MutableStateFlow<List<IconModel>>(emptyList())
    val icons: Flow<List<IconModel>> = _icons

    private val _account = MutableStateFlow<AccountModel?>(null)
    val account: Flow<AccountModel?> = _account

    init {
        getAllIcon()
        getAccountTypeList()
    }

    private fun getAccountTypeList() {
        viewModelScope.launch(Dispatchers.IO) {
            val enumValues: List<String> = AccountType.entries.map { it.value }
            val currencyData: List<String> = CurrencyType.entries.map { it.value }
            setState(
                getCurrentState().copy(
                    accountTypeList = enumValues,
                    currencyData = currencyData
                )
            )
        }
    }

    private fun getAllIcon() {
        viewModelScope.launch(Dispatchers.IO) {
            val transferIcons =
                context.resources.getStringArray(com.mismayilov.uikit.R.array.transfer_icons)
            transferIcons.map { name ->
                IconModel(
                    name = "",
                    icon = name,
                    type = IconType.ACCOUNT.name
                )
            }.let {
                _icons.value = it
                setState(getCurrentState().copy(icons = _icons.value))
            }
        }
    }

    override fun onEventChanged(event: CreateAccountEvent) {
        when (event) {
            is CreateAccountEvent.CreateAccount -> {
                if (event.accountName.isEmpty() || event.accountType.isEmpty() || event.currency.isEmpty() || event.balance.isEmpty()) {
                    setEffect(CreateAccountEffect.ShowToast("Please fill all fields"))
                } else {
                    createAccount(
                        event.accountName,
                        event.accountType,
                        event.iconPosition,
                        event.currency,
                        event.balance,
                        event.isUpdate
                    )
                }
            }
            is CreateAccountEvent.GetAccount -> {
                getAccount(event.id)
            }

        }
    }

    private fun getAccount(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            getAccountByIDUseCase(id)
                .onEach {
                    _account.value = it
                    setState(getCurrentState().copy(account = _account.value))
                }.launchIn(viewModelScope)
        }
    }

    private fun createAccount(
        name: String,
        type: String,
        iconPosition: Int,
        currency: String,
        balance: String,
        isUpdate: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val currencyName = CurrencyType.entries.find { it.value == currency }?.name!!
            createAccountUseCase(
                AccountModel(
                    name = name,
                    currency = currencyName,
                    amount = balance.toDouble(),
                    amountUsd = balance.toDouble(),
                    icon = _icons.value[iconPosition].icon,
                    type = type,
                    id = _account.value?.id ?: 0
                ), isUpdate
            )
            setEffect(CreateAccountEffect.ShowToast(if (isUpdate) "Account updated" else "Account created"))
            setEffect(CreateAccountEffect.CloseFragment)
        }
    }

}