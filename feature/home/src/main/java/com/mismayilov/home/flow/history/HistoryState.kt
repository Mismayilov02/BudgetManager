package com.mismayilov.home.flow.history

data class HistoryState (
    val isLoading: Boolean = false,
    val history: List<String> = emptyList()
) {
}