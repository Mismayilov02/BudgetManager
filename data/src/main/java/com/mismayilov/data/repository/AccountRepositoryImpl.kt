package com.abbtech.firstabbtechapp.data.repository

import com.mismayilov.data.database.dao.AccountDao
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val transactionAccountDao: AccountDao
//    private val noteService: NoteService
)
    : AccountRepository {
    override fun getAccounts(): Flow<List<AccountModel>> {
        return transactionAccountDao.getAll()
    }

    override fun addAccount(transaction: AccountModel) {
        transactionAccountDao.insert(transaction)
    }

    override fun removeAccount(id: Long) {
        transactionAccountDao.deleteById(id)
    }

    override fun updateAccount(transaction: AccountModel) {
        transactionAccountDao.update(transaction)
    }

}