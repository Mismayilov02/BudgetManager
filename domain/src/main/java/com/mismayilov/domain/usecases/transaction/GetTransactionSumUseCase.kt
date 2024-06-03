package com.mismayilov.domain.usecases.transaction

import com.mismayilov.domain.repositories.TransactionRepository
import javax.inject.Inject

class GetTransactionSumUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
     operator fun invoke(type: String): Double {
        return transactionRepository.getSum(type)
    }
}