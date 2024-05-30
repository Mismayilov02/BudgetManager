package com.mismayilov.icon.flow.create_icon

import com.mismayilov.domain.entities.local.IconModel

data class CreateIconState(
    val icons: List<IconModel>? = null,
)