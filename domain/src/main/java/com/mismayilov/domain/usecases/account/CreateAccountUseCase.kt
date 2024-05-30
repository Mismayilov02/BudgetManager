package com.mismayilov.domain.usecases.account

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.AccountRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
     suspend operator fun invoke(account: AccountModel, isUpdate: Boolean) {
        if (!isUpdate)accountRepository.addAccount(account)
        else accountRepository.updateAccount(account)
    }
}