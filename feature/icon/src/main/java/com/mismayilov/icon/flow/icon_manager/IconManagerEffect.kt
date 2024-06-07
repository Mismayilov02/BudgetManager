package com.mismayilov.icon.flow.icon_manager

sealed class IconManagerEffect {
    data class ShowToast(val message: String) : IconManagerEffect()
}