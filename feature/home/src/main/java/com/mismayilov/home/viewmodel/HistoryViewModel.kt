package com.mismayilov.home.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.mismayilov.common.utility.Util.Companion.ONE_WEEK_IN_MILLIS
import com.mismayilov.domain.usecases.transaction.GetAllTransactionUseCase
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.domain.usecases.transaction.DeleteTransactionUseCase
import com.mismayilov.home.flow.history.HistoryEffect
import com.mismayilov.home.flow.history.HistoryEvent
import com.mismayilov.home.flow.history.HistoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllTransactionUseCase: GetAllTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel<HistoryState, HistoryEvent, HistoryEffect>() {
    override fun getInitialState(): HistoryState = HistoryState()

    init {
        setEvent(HistoryEvent.GetHistory(System.currentTimeMillis() - ONE_WEEK_IN_MILLIS, System.currentTimeMillis()))
    }

    override fun onEventChanged(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.GetHistory -> getHistory(event.startDate, event.endDate)
            is HistoryEvent.DeleteTransaction -> deleteTransaction(event.id)
        }
    }

    private fun deleteTransaction(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTransactionUseCase.invoke(id)
        }
    }

    private fun getHistory(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            if (startDate > endDate) {
                setEffect(HistoryEffect.ShowToast(context.getString(com.mismayilov.uikit.R.string.start_date_error)))
                return@launch
            }else if (endDate>System.currentTimeMillis()){
                setEffect(HistoryEffect.ShowToast(context.getString(com.mismayilov.uikit.R.string.end_date_error)))
                return@launch
            }
            getAllTransactionUseCase(startDate, endDate).collect {
                setState(getCurrentState().copy(history = it))
            }
        }
    }

}