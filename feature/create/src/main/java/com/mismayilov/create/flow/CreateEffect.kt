package com.mismayilov.create.flow

import com.mismayilov.domain.entities.local.IconModel

sealed class CreateEffect {
    data object CloseScreen : CreateEffect()
    data class ShowBottomSheet(val isTop: Boolean, val cardData:List<IconModel>) : CreateEffect()
    data class ShowToast(val message: String) : CreateEffect()
}