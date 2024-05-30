package com.mismayilov.icon.flow.icon_manager

import com.mismayilov.domain.entities.local.IconModel

data class IconManagerState (
    val icons: List<IconModel>? = null,
)