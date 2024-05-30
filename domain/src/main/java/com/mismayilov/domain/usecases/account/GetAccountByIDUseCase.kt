package com.mismayilov.domain.usecases.account

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountByIDUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
     operator fun invoke(id: Long): Flow<AccountModel> {
        return accountRepository.getAccountById(id)
    }
}