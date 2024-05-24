package com.mismayilov.domain.repositories

import com.mismayilov.domain.entities.local.AccountModel
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<List<AccountModel>>
    fun addAccount(transaction: AccountModel)
    fun removeAccount(id: Long)
    fun updateAccount(transaction: AccountModel)
}