package com.mismayilov.domain.usecases.account

import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.repositories.IconRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllIconUseCase @Inject constructor(
    private val iconRepository: IconRepository
) {
    operator fun invoke(): Flow<List<IconModel>> {
        return iconRepository.getIcons()
    }
}