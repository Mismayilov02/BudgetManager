package com.mismayilov.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mismayilov.domain.entities.local.AccountModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactionAccount: AccountModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertNoSuspend(transactionAccount: AccountModel)

    @Query("SELECT * FROM account ORDER BY is_pinned DESC, id DESC")
    fun getAll():Flow<List<AccountModel>>

    @Query("SELECT * FROM account WHERE id = :id")
     fun getById(id: Long): Flow<AccountModel>

    @Query("SELECT * FROM account WHERE is_pinned = :isPinned")
    fun getAccountByPin(isPinned: Boolean): Flow<List<AccountModel>>

    @Query("DELETE FROM account WHERE id = :id")
   suspend fun deleteById(id: Long)

    @Update
    suspend fun update(transactionAccount: AccountModel)

    @Query("UPDATE account SET is_pinned = CASE WHEN id = :id THEN 1 ELSE 0 END")
    suspend fun updateAccountPin(id: Long)

    @Query("UPDATE account SET amount = amount + :amount WHERE id = :id")
    suspend fun updateAccountAmount(id: Long, amount: Double)
}