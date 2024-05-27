package com.abbtech.firstabbtechapp.domain.usecases

import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.repositories.IconRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val categoryRepository: IconRepository
) {
     operator fun invoke(category: IconModel) {
         categoryRepository.addIcon(category)
     }
}