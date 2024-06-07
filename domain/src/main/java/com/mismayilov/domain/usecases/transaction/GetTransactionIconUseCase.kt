package com.mismayilov.domain.usecases.transaction

import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.repositories.TransactionRepository
import javax.inject.Inject

class GetTransactionIconUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(icon: IconModel) {
        return transactionRepository.updateTransactionIcon(icon)
    }
}