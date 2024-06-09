package com.mismayilov.domain.usecases.icon

import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.repositories.IconRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllIconUseCase @Inject constructor(
    private val iconRepository: IconRepository
) {
    operator fun invoke(type:String? = null): Flow<List<IconModel>> {
        if (type == null) return iconRepository.getIcons()
        return iconRepository.getIconByType(type)
    }
}