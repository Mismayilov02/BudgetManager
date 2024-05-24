package com.mismayilov.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mismayilov.domain.entities.local.CategoryModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transactionCategory: CategoryModel)

    @Update
    fun update(transactionCategory: CategoryModel)

    @Query("SELECT * FROM category")
    fun getAll(): Flow<List<CategoryModel>>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getById(id: Long): Flow<CategoryModel>

    @Query("DELETE FROM category WHERE id = :id")
    fun deleteById(id: Long)
}