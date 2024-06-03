package com.mismayilov.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.domain.repositories.TransactionRepository
import com.mismayilov.domain.usecases.transaction.GetAllTransactionUseCase
import com.mismayilov.domain.usecases.transaction.GetTransactionUseCase
import com.mismayilov.home.flow.transaction_view.TransactionViewEffect
import com.mismayilov.home.flow.transaction_view.TransactionViewEvent
import com.mismayilov.home.flow.transaction_view.TransactionViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewViewModel @Inject constructor(
    private val getTransactionUseCase: GetTransactionUseCase
) : BaseViewModel<TransactionViewState, TransactionViewEvent, TransactionViewEffect>() {
    override fun getInitialState(): TransactionViewState = TransactionViewState()

    override fun onEventChanged(event: TransactionViewEvent) {
        when (event) {
            is TransactionViewEvent.GetTransaction -> {
                getTransaction(event.id)
            }
        }
    }

    private fun getTransaction(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            getTransactionUseCase(id).collect {
                setState(getCurrentState().copy(transaction = it))
            }
        }
    }
}