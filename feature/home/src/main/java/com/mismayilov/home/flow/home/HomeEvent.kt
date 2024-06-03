package com.mismayilov.home.flow.home

sealed class HomeEvent {
    data class DeleteTransaction(val id: Long) : HomeEvent()
}