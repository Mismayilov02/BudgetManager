package com.mismayilov.create.flow

import com.mismayilov.domain.entities.local.IconModel

sealed class CreateEffect {
    data class SelectCardData(val isTop: Boolean, val cardData:List<IconModel>) : CreateEffect()
}