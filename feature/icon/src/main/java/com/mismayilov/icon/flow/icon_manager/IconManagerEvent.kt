package com.mismayilov.icon.flow.icon_manager

import com.mismayilov.common.unums.IconType

sealed class IconManagerEvent {
    data class IconTypeSelected(val iconType: IconType) : IconManagerEvent()
    data class DeleteIcon(val id: Long) : IconManagerEvent()
}