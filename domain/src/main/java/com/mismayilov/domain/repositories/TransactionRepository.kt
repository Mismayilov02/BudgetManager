package com.abbtech.firstabbtechapp.domain.repositories

import com.mismayilov.domain.entities.local.TransactionModel
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(): Flow<List<TransactionModel>>
    fun addTransaction(transaction: TransactionModel)
    fun removeTransaction(transaction: TransactionModel)
    fun updateTransaction(transaction: TransactionModel)
}