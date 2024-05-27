package com.mismayilov.home.flow.home

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.TransactionModel

data class HomeState (
    val transactionList: List<TransactionModel>? = null,
    val accountData: AccountModel? = null
)