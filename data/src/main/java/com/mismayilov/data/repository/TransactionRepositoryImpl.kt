package com.mismayilov.data.repository

import com.mismayilov.domain.repositories.TransactionRepository
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.data.database.dao.TransactionDao
import com.mismayilov.domain.entities.local.TransactionAmountsModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {


    override fun getTransactionWithLimit(limit: Int): Flow<List<TransactionModel>> {
        return transactionDao.getWithLimit(limit)
    }

    override fun getAllTransactions(): Flow<List<TransactionModel>> {
        return transactionDao.getAll()
    }

    override fun getTransactionById(id: Long): Flow<TransactionModel> {
        return transactionDao.getById(id)
    }

    override fun getTransactionByTimeRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionModel>> {
        return transactionDao.getTransactionByTimeRange(startDate, endDate)
    }

    override suspend fun addTransaction(transaction: TransactionModel) {
        transactionDao.insert(transaction)
    }

    override fun removeTransaction(transaction: TransactionModel) {
        transactionDao.delete(transaction)
    }

    override suspend fun updateTransaction(transaction: TransactionModel) {
        transactionDao.update(transaction)
    }

    override suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteTransactionAndUpdateAccount(id)
    }

    override fun getSumExpenseByAccount(): Flow<Double> {
        return transactionDao.getSumExpense()
    }

    override fun getSumIncomeByAccount(): Flow<Double> {
        return transactionDao.getSumIncome()
    }

    override fun getSumExpense(): Flow<Double> {
        return transactionDao.getExpenseAmount()
    }

    override fun getSumIncome(): Flow<Double> {
        return transactionDao.getIncomeAmount()
    }

    override fun getSumTransfer(): Flow<Double> {
        return transactionDao.getTransferAmount()
    }


}