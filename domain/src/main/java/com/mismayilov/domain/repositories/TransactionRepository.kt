package com.mismayilov.domain.repositories

import com.mismayilov.domain.entities.local.TransactionModel
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactionWithLimit(limit: Int): Flow<List<TransactionModel>>
    fun getAllTransactions(): Flow<List<TransactionModel>>
    fun getTransactionByTimeRange(startDate: Long, endDate: Long): Flow<List<TransactionModel>>
    fun addTransaction(transaction: TransactionModel)
    fun removeTransaction(transaction: TransactionModel)
    fun updateTransaction(transaction: TransactionModel)
}