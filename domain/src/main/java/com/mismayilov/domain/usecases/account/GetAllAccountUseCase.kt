package com.mismayilov.domain.usecases.account

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(isPinned:Boolean = false): Flow<List<AccountModel>> {
        if (isPinned) return accountRepository.getAccountByPin(true)
        return accountRepository.getAccounts()
    }
}