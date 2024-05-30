package com.mismayilov.domain.repositories

import com.mismayilov.domain.entities.local.IconModel
import kotlinx.coroutines.flow.Flow

interface IconRepository {
    fun getIcons(): Flow<List<IconModel>>
    fun getIconByType(type: String): Flow<List<IconModel>>
    suspend fun addIcon(transactionCategory: IconModel)
    suspend fun deleteIcon(id: Long)
    suspend fun updateIcon(transactionCategory: IconModel)
}