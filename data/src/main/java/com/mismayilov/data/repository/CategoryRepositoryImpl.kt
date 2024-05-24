package com.abbtech.firstabbtechapp.data.repository

import com.mismayilov.data.database.dao.CategoryDao
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.CategoryModel
import com.mismayilov.domain.repositories.AccountRepository
import com.mismayilov.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
//    private val noteService: NoteService
)
    : CategoryRepository {

    override fun getCategories(): Flow<List<CategoryModel>> {
        return categoryDao.getAll()
    }

    override fun addCategory(transactionCategory: CategoryModel) {
        categoryDao.insert(transactionCategory)
    }

    override fun removeCategory(id: Long) {
        categoryDao.deleteById(id)
    }

    override fun updateCategory(transactionCategory: CategoryModel) {
        categoryDao.update(transactionCategory)
    }

}