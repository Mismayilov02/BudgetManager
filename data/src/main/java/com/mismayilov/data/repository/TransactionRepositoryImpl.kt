package com.abbtech.firstabbtechapp.data.repository

import com.abbtech.firstabbtechapp.domain.repositories.TransactionRepository
import com.mismayilov.domain.entities.local.TransactionModel
import com.theternal.data.database.dao.TransactionDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
//    private val noteService: NoteService
)
    : TransactionRepository {
    override fun getTransactions(): Flow<List<TransactionModel>> {
        return transactionDao.getAll()
    }

    override fun addTransaction(transaction: TransactionModel) {
        transactionDao.insert(transaction)
    }

    override fun removeTransaction(transaction: TransactionModel) {
        transactionDao.delete(transaction)
    }

    override fun updateTransaction(transaction: TransactionModel) {
        transactionDao.update(transaction)
    }

}