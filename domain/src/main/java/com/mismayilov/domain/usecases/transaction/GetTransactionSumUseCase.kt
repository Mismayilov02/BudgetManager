package com.mismayilov.domain.usecases.transaction

import com.mismayilov.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionSumUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
     operator fun invoke(type: String): Flow<Double> {
         if (type == "INCOME") return transactionRepository.getSumIncomeByAccount()
         return transactionRepository.getSumExpenseByAccount()
    }
}