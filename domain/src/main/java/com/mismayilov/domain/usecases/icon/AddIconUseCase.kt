package com.mismayilov.domain.usecases.icon

import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.repositories.IconRepository
import javax.inject.Inject

class AddIconUseCase @Inject constructor(
    private val iconRepository: IconRepository
) {
    suspend operator fun invoke(category: IconModel, isUpdate: Boolean = false) {
        if (!isUpdate) iconRepository.addIcon(category)
        else iconRepository.updateIcon(category)
    }
}