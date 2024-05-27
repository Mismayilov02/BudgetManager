package com.mismayilov.domain.repositories

import com.mismayilov.domain.entities.local.AccountModel
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<List<AccountModel>>
    fun getAccountByPin(isPinned: Boolean): Flow<List<AccountModel>>
    fun addAccount(accountModel: AccountModel)
    fun deleteAccount(id: Long)
    fun updateAccount(accountModel: AccountModel)
}