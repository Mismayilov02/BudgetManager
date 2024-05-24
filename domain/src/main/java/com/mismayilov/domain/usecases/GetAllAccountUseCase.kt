package com.abbtech.firstabbtechapp.domain.usecases

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<List<AccountModel>> {
        return accountRepository.getAccounts()
    }
}