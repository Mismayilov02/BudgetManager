package com.mismayilov.auth.flow.pin

data class PinState(
    val pinLength: Int = 0,
    val isPinCorrect: Boolean = false
) {
}