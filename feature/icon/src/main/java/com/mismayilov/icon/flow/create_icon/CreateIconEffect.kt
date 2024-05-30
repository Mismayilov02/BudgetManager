package com.mismayilov.icon.flow.create_icon

sealed class CreateIconEffect {
    data object CloseFragment : CreateIconEffect()
    data class ShowToast(val message: String) : CreateIconEffect()
}