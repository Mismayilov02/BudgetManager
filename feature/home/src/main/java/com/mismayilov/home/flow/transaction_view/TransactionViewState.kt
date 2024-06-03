package com.mismayilov.home.flow.transaction_view

import com.mismayilov.domain.entities.local.TransactionModel

data class TransactionViewState (
    val transaction: TransactionModel? = null,
) {
}