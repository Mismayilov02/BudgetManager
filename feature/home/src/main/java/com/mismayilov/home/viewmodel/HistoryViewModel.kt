package com.mismayilov.home.viewmodel

import com.abbtech.firstabbtechapp.domain.usecases.GetAllTransactionUseCase
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.home.flow.history.HistoryEffect
import com.mismayilov.home.flow.history.HistoryEvent
import com.mismayilov.home.flow.history.HistoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllTransactionUseCase: GetAllTransactionUseCase
):BaseViewModel<HistoryState, HistoryEvent, HistoryEffect>() {
    override fun getInitialState(): HistoryState = HistoryState()
}