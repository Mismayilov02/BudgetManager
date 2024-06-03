package com.mismayilov.domain.usecases.account

import com.mismayilov.domain.repositories.AccountRepository
import javax.inject.Inject

class UpdateAccountAmountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(id: Long, amount: Double) {
        accountRepository.updateAccountAmount(id, amount)
    }
}