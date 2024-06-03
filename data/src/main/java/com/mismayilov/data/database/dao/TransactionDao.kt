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
    suspend fun insert(vararg note: TransactionModel)

    @Query("SELECT * FROM 'transaction' ORDER BY date DESC")
    fun getAll(): Flow<List<TransactionModel>>

    @Query("SELECT * FROM 'transaction' WHERE id=:id")
    fun getById(id: Long): Flow<TransactionModel>

    @Query("SELECT * FROM 'transaction' ORDER BY date DESC LIMIT :limit")
    fun getWithLimit(limit: Int): Flow<List<TransactionModel>>

    @Query("SELECT * FROM 'transaction' WHERE date >= :startDate AND date <= :endDate")
    fun getTransactionByTimeRange(startDate: Long, endDate: Long): Flow<List<TransactionModel>>

    @Query(
        """
    SELECT SUM(t.amount)
    FROM `transaction` t
    WHERE t.category_id LIKE '%' || :type || '%'
    AND t.account_id LIKE '%'|| (SELECT id  FROM account WHERE is_pinned = 1) || '%'""")
     fun getSum(type: String): Double

    @Delete
    fun delete(note: TransactionModel)

    @Query("DELETE FROM 'transaction' WHERE id=:id")
    suspend fun deleteTransaction(id: Long)

    @Update
    fun update(note: TransactionModel)
}