package com.mismayilov.auth.flow.pin

sealed class PinEffect {
    data class ShowToast(val message: String) : PinEffect()
    data object NavigateToMain : PinEffect()
    data object NavigateToHome : PinEffect()
    data object NavigateToBack : PinEffect()
}