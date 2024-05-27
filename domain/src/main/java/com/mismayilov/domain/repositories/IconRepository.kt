package com.mismayilov.domain.repositories

import com.mismayilov.domain.entities.local.IconModel
import kotlinx.coroutines.flow.Flow

interface IconRepository {
    fun getIcons():Flow<List<IconModel>>
    fun addIcon(transactionCategory: IconModel)
    fun removeIcon(id: Long)
    fun updateIcon(transactionCategory: IconModel)
}