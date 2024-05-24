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
    fun insert(transactionAccount: AccountModel)

    @Query("SELECT * FROM account")
    fun getAll():Flow<List<AccountModel>>

    @Query("SELECT * FROM account WHERE id = :id")
    fun getById(id: Long): Flow<AccountModel>

    @Query("DELETE FROM account WHERE id = :id")
    fun deleteById(id: Long)

    @Update
    fun update(transactionAccount: AccountModel)
}