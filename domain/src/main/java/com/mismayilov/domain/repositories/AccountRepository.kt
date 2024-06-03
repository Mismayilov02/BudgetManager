package com.mismayilov.domain.repositories

import com.mismayilov.domain.entities.local.AccountModel
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<List<AccountModel>>
    fun getAccountByPin(isPinned: Boolean): Flow<List<AccountModel>>
    fun getAccountById(id: Long): Flow<AccountModel>
    suspend fun addAccount(accountModel: AccountModel)
    suspend fun deleteAccount(id: Long)
    suspend fun updateAccount(accountModel: AccountModel)
    suspend fun updateAccountPin(pinedAccountId: Long)
    suspend fun updateAccountAmount(id: Long, amount: Double)
}