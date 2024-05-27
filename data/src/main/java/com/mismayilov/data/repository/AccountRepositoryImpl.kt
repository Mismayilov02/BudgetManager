package com.mismayilov.data.repository

import com.mismayilov.data.database.dao.AccountDao
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
//    private val noteService: NoteService
)
    : AccountRepository {
    override fun getAccounts(): Flow<List<AccountModel>> {
        return accountDao.getAll()
    }

    override fun getAccountByPin(isPinned: Boolean): Flow<List<AccountModel>> {
        return accountDao.getAccountByPin(isPinned)
    }

    override fun addAccount(accountModel: AccountModel) {
        accountDao.insert(accountModel)
    }

    override fun deleteAccount(id: Long) {
        accountDao.deleteById(id)
    }


    override fun updateAccount(accountModel: AccountModel) {
        accountDao.update(accountModel)
    }

}