package com.mismayilov.domain.usecases.transaction

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.TransactionRepository
import javax.inject.Inject

class TransactionAccountUpdateUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(account: AccountModel) {
        transactionRepository.updateTransactionAccount(account)
    }
}