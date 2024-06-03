package com.mismayilov.domain.usecases.transaction

import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
     operator fun invoke(id: Long): Flow<TransactionModel> {
        return transactionRepository.getTransactionById(id)
    }
}