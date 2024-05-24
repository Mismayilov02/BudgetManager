package com.abbtech.firstabbtechapp.domain.usecases

import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.CategoryModel
import com.mismayilov.domain.repositories.AccountRepository
import com.mismayilov.domain.repositories.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
     operator fun invoke(category: CategoryModel) {
         categoryRepository.addCategory(category)
     }
}