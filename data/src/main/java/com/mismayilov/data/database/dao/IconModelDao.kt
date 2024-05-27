package com.mismayilov.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mismayilov.domain.entities.local.IconModel
import kotlinx.coroutines.flow.Flow

@Dao
interface IconModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transactionCategory: IconModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(transactionCategories: List<IconModel>)

    @Update
    fun update(transactionCategory: IconModel)

    @Query("SELECT * FROM icon")
    fun getAll(): Flow<List<IconModel>>

    @Query("SELECT * FROM icon WHERE id = :id")
    fun getById(id: Long): Flow<IconModel>

    @Query("DELETE FROM icon WHERE id = :id")
    fun deleteById(id: Long)
}