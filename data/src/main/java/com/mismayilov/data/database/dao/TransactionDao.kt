package com.mismayilov.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mismayilov.domain.entities.local.TransactionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg note: TransactionModel)

    @Query("SELECT * FROM 'transaction'")
    fun getAll(): Flow<List<TransactionModel>>

    @Query("SELECT * FROM 'transaction' WHERE id=:id")
    fun getById(id: Long): Flow<TransactionModel>

    @Query("SELECT * FROM 'transaction' LIMIT :limit")
    fun getWithLimit(limit: Int): Flow<List<TransactionModel>>

    @Query("SELECT * FROM 'transaction' WHERE date >= :startDate AND date <= :endDate")
    fun getTransactionByTimeRange(startDate: Long, endDate: Long): Flow<List<TransactionModel>>

    @Delete
    fun delete(note: TransactionModel)

    @Update
    fun update(note: TransactionModel)
}