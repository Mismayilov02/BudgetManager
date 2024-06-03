package com.mismayilov.domain.usecases.transaction

import com.mismayilov.domain.repositories.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: Long) {
        transactionRepository.deleteTransaction(id)
    }
}