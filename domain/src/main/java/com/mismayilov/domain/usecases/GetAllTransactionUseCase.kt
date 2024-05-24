package com.abbtech.firstabbtechapp.domain.usecases

import com.abbtech.firstabbtechapp.domain.repositories.TransactionRepository
import com.mismayilov.domain.entities.local.TransactionModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<TransactionModel>> {
        return transactionRepository.getTransactions()
    }
}