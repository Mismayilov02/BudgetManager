package com.mismayilov.domain.usecases.icon

import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.repositories.IconRepository
import javax.inject.Inject


class DeleteIconUseCase @Inject constructor(
    private val iconRepository: IconRepository
) {
    suspend operator fun invoke(id: Long) {
        iconRepository.deleteIcon(id)
    }
}