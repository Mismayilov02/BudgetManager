package com.abbtech.firstabbtechapp.domain.usecases

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.AccountRepository
import javax.inject.Inject

class AddAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
     operator fun invoke(account: AccountModel) {
        accountRepository.addAccount(account)
    }
}