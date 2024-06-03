package com.mismayilov.home.flow.transaction_view

sealed class TransactionViewEvent{
    data class GetTransaction(val id: Long): TransactionViewEvent()
}