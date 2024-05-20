package com.theternal.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mismayilov.domain.entities.local.TransactionHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: TransactionHistory)

    @Query("SELECT * FROM transaction_history")
    fun getAll(): Flow<List<TransactionHistory>>

    @Query("SELECT * FROM transaction_history WHERE id=:id")
    fun getById(id: Long): Flow<TransactionHistory>

    @Delete
    fun delete(note: TransactionHistory)

    @Update
    fun update(note: TransactionHistory)
}