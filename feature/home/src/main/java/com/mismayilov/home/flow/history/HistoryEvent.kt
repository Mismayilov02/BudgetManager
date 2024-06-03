package com.mismayilov.home.flow.history

sealed class HistoryEvent {
    data class GetHistory(val startDate: Long, val endDate: Long) : HistoryEvent()
   data class DeleteTransaction(val id: Long) : HistoryEvent()
}