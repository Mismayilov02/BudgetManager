package com.mismayilov.auth.flow.pin

sealed class PinEvent {
    data class NumberClicked(val number: String) : PinEvent()
    data object DeleteClicked : PinEvent()
}