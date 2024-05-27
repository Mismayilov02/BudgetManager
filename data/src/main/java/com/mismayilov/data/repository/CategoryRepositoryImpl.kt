package com.abbtech.firstabbtechapp.data.repository

import com.mismayilov.data.database.dao.IconModelDao
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.repositories.IconRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val iconModelDao: IconModelDao
//    private val noteService: NoteService
) : IconRepository {

    override fun getIcons(): Flow<List<IconModel>> {
        return iconModelDao.getAll()
    }

    override fun addIcon(transactionCategory: IconModel) {
        iconModelDao.insert(transactionCategory)
    }

    override fun removeIcon(id: Long) {
        iconModelDao.deleteById(id)
    }

    override fun updateIcon(transactionCategory: IconModel) {
        iconModelDao.update(transactionCategory)
    }

}