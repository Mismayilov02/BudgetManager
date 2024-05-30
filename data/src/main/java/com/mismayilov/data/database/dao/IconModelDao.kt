package com.mismayilov.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mismayilov.domain.entities.local.IconModel
import kotlinx.coroutines.flow.Flow

@Dao
interface IconModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactionCategory: IconModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(transactionCategories: List<IconModel>)

    @Update
    suspend fun update(transactionCategory: IconModel)

    @Query("SELECT * FROM icon ORDER BY id DESC")
    fun getAll(): Flow<List<IconModel>>

    @Query("SELECT * FROM icon WHERE type = :type")
    fun getByType(type: String): Flow<List<IconModel>>

    @Query("SELECT * FROM icon WHERE id = :id")
    fun getById(id: Long): Flow<IconModel>

    @Query("DELETE FROM icon WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    fun delete(transactionCategory: IconModel)
}