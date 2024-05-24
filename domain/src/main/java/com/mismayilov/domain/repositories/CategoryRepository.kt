package com.mismayilov.domain.repositories

import com.mismayilov.domain.entities.local.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories():Flow<List<CategoryModel>>
    fun addCategory(transactionCategory: CategoryModel)
    fun removeCategory(id: Long)
    fun updateCategory(transactionCategory: CategoryModel)
}