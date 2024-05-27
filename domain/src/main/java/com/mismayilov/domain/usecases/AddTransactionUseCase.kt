package com.mismayilov.domain.usecases

import com.mismayilov.domain.repositories.TransactionRepository
import com.mismayilov.domain.entities.local.TransactionModel
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
     operator fun invoke(transaction: TransactionModel) {
        transactionRepository.addTransaction(transaction)
    }
}