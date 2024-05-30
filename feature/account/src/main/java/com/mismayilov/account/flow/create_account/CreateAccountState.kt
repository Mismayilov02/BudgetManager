package com.mismayilov.account.flow.create_account

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.IconModel

data class CreateAccountState(
    val icons:List<IconModel>? = null,
    val currencyData:List<String>? = null,
    val accountTypeList: List<String>? = null,
    val account: AccountModel? = null
)