package com.mismayilov.home.flow.history

import com.mismayilov.domain.entities.local.TransactionModel

data class HistoryState (
    val history: List<TransactionModel>? = null
)