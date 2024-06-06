package com.mismayilov.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.mismayilov.domain.usecases.account.GetAllAccountUseCase
import com.mismayilov.domain.usecases.transaction.GetAllTransactionUseCase
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.domain.usecases.transaction.DeleteTransactionUseCase
import com.mismayilov.domain.usecases.transaction.GetTransactionSumUseCase
import com.mismayilov.home.flow.home.HomeEffect
import com.mismayilov.home.flow.home.HomeEvent
import com.mismayilov.home.flow.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTransactionUseCase: GetAllTransactionUseCase,
    private val getAllAccountUseCase: GetAllAccountUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getTransactionSumUseCase: GetTransactionSumUseCase
): BaseViewModel<HomeState, HomeEvent, HomeEffect>() {
    override fun getInitialState(): HomeState = HomeState()

    private val _transactionList = MutableStateFlow<List<TransactionModel>?>(null)
    private val _accountData = MutableStateFlow<AccountModel?>(null)
    private val _expenseSum = MutableStateFlow<Double?>(null)
    private val _incomeSum = MutableStateFlow<Double?>(null)

    override fun onEventChanged(event: HomeEvent) {
        when(event) {
            is HomeEvent.DeleteTransaction -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteTransactionUseCase(event.id)
                }
            }
        }
    }

    init {
        getAllTransaction()
        getTransactionSum()
        getAccount()
    }

    private fun updateStateIfNeeded() {
        val transactionList = _transactionList.value
        val accountData = _accountData.value
        val expenseSum = _expenseSum.value
        val incomeSum = _incomeSum.value

        if (transactionList != null && accountData != null && expenseSum != null && incomeSum != null) {
            setState(getCurrentState().copy(
                transactionList = transactionList,
                accountData = accountData,
                expenseSum = expenseSum,
                incomeSum = incomeSum
            ))
        }
    }

    private fun getTransactionSum() {
        viewModelScope.launch(Dispatchers.IO) {
            val expenseSum = getTransactionSumUseCase("EXPENSE")
            val incomeSum = getTransactionSumUseCase("INCOME")

            combine(expenseSum, incomeSum) { expense, income ->
                _expenseSum.value = expense
                _incomeSum.value = income
            }.collect {
                updateStateIfNeeded()
            }
        }
    }

    private fun getAllTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTransactionUseCase().collect {
                _transactionList.value = it
                updateStateIfNeeded()
            }
        }
    }

    private fun getAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllAccountUseCase(true).collect {
                _accountData.value = it.firstOrNull()
                updateStateIfNeeded()
            }
        }
    }

}