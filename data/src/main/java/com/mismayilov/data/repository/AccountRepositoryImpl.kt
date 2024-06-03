package com.mismayilov.data.repository

import com.mismayilov.data.database.dao.AccountDao
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
)
    : AccountRepository {
    override fun getAccounts(): Flow<List<AccountModel>> {
        return accountDao.getAll()
    }

    override fun getAccountByPin(isPinned: Boolean): Flow<List<AccountModel>> {
        return accountDao.getAccountByPin(isPinned)
    }

    override  fun getAccountById(id: Long): Flow<AccountModel> {
        return accountDao.getById(id)
    }

    override suspend fun addAccount(accountModel: AccountModel) {
        accountDao.insert(accountModel)
    }

    override suspend fun deleteAccount(id: Long) {
        accountDao.deleteById(id)
    }


    override suspend fun updateAccount(accountModel: AccountModel) {
        accountDao.update(accountModel)
    }

    override suspend fun updateAccountPin(pinedAccountId: Long) {
        accountDao.updateAccountPin(pinedAccountId)
    }

    override suspend fun updateAccountAmount(id: Long, amount: Double) {
        accountDao.updateAccountAmount(id, amount)
    }

}