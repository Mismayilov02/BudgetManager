package com.mismayilov.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.mismayilov.domain.usecases.account.GetAllAccountUseCase
import com.abbtech.firstabbtechapp.domain.usecases.GetAllTransactionUseCase
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.home.flow.home.HomeEffect
import com.mismayilov.home.flow.home.HomeEvent
import com.mismayilov.home.flow.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTransactionUseCase: GetAllTransactionUseCase,
    private val getAllAccountUseCase: GetAllAccountUseCase
): BaseViewModel<HomeState, HomeEvent, HomeEffect>() {
    override fun getInitialState(): HomeState = HomeState()

    init {
        getAllTransaction()
        getAccount()
    }

    private fun getAccount() {
        viewModelScope.launch (Dispatchers.IO) {
            getAllAccountUseCase(true).onEach {
                setState(getCurrentState().copy(accountData = it[0]))
            }.launchIn(viewModelScope)
        }
    }

    private fun getAllTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTransactionUseCase().onEach {
                setState(getCurrentState().copy(transactionList = it))
            }.launchIn(viewModelScope)
        }
    }
}