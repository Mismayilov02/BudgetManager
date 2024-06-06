package com.mismayilov.reports.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mismayilov.common.extensions.toCurrencyString
import com.mismayilov.common.unums.CurrencyType
import com.mismayilov.common.unums.IconType
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.usecases.account.GetAllAccountUseCase
import com.mismayilov.domain.usecases.remote.GetExchangeUseCase
import com.mismayilov.domain.usecases.transaction.GetTransactionAmountUseCase
import com.mismayilov.reports.flow.ReportsEffect
import com.mismayilov.reports.flow.ReportsEvent
import com.mismayilov.reports.flow.ReportsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.times
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getAllAccountUseCase: GetAllAccountUseCase,
    private val getAmountsUseCase: GetTransactionAmountUseCase,
    private val getExchangeUseCase: GetExchangeUseCase
) : BaseViewModel<ReportsState, ReportsEvent, ReportsEffect>() {
    override fun getInitialState(): ReportsState = ReportsState()

    private val _accounts = MutableStateFlow<List<AccountModel>>(emptyList())
    private val _totalAmount = MutableStateFlow(0.0)
    private val _expenseAmount = MutableStateFlow(0.0)
    private val _incomeAmount = MutableStateFlow(0.0)
    private val _transactionAmount = MutableStateFlow(0.0)

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            val expenseAmount = getAmountsUseCase("EXPENSE")
            val incomeAmount = getAmountsUseCase("INCOME")
            val transactionAmount = getAmountsUseCase("ACCOUNT")
            val accounts = getAllAccountUseCase()

            combine(expenseAmount, incomeAmount, transactionAmount, accounts) { expense, income, transaction, accountsList ->
                Triple(expense, income, transaction) to accountsList
            }.collect { (amounts, accountsList) ->
                _expenseAmount.value = amounts.first
                _incomeAmount.value = amounts.second
                _transactionAmount.value = amounts.third

                _accounts.value = accountsList
                val iconModels = accountsList.map { account ->
                    IconModel(
                        account.name, account.icon,
                        IconType.ACCOUNT.name,
                        "${account.amount.toCurrencyString()} ${CurrencyType.valueOf(account.currency).symbol}"
                    )
                }
                setState(getCurrentState().copy(accounts = iconModels))

                sumUsdAmount(accountsList)
            }
        }
    }

    private fun sumUsdAmount(modelList: List<AccountModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            modelList.forEachIndexed { index, account ->
                if (account.currency == CurrencyType.USD.name) {
                    account.amountUsd = account.amount
                } else {
                    getExchangeRate(CurrencyType.valueOf(account.currency).name, account.amount, index)
                }
            }
        }
    }

    private fun getExchangeRate(currency: String, amount: Double, index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            invoke(kSuspendFunction = { getExchangeUseCase(currency, "USD") },
                onError = { e -> Log.e("FetchCurrencyRate", "Error: $e") },
                onSuccess = { result ->
                    _accounts.value[index].amountUsd = (amount * result.toDouble())
                    sumTotalAmount()
                })
        }
    }

    private fun sumTotalAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            _totalAmount.value = _accounts.value.sumByDouble { it.amountUsd }
            setState(getCurrentState().copy(
                totalAmount = _totalAmount.value,
                expenseAmount = _expenseAmount.value,
                incomeAmount = _incomeAmount.value,
                transactionAmount = _transactionAmount.value
            ))
        }
    }
}
