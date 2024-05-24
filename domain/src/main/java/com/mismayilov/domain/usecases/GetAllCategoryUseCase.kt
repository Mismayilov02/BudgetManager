package com.abbtech.firstabbtechapp.domain.usecases

import com.abbtech.firstabbtechapp.domain.repositories.TransactionRepository
import com.mismayilov.domain.entities.local.CategoryModel
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoryUseCase @Inject constructor(
    private val ategoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<CategoryModel>> {
        return ategoryRepository.getCategories()
    }
}