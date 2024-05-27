package com.mismayilov.domain.usecases.account

import com.mismayilov.domain.repositories.AccountRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(accountId: Long) {
        accountRepository.deleteAccount(accountId)
    }
}