package com.mismayilov.settings.flow.accounts

import com.mismayilov.domain.entities.local.AccountModel

data class AccountsState(
    val accounts: List<AccountModel>? = null
)