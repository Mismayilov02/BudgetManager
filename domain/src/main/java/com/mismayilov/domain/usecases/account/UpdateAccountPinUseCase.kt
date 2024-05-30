package com.mismayilov.domain.usecases.account

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.AccountRepository
import javax.inject.Inject


class UpdateAccountPinUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
     suspend operator fun invoke(pinedAccountId:Long) {
        accountRepository.updateAccountPin(pinedAccountId)
    }
}