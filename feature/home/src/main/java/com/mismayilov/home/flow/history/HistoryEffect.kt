package com.mismayilov.home.flow.history

sealed class HistoryEffect {
   data class ShowToast(val message: String) : HistoryEffect()
}