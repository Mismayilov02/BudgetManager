package com.mismayilov.create.flow

import com.mismayilov.common.unums.IconType

sealed class CreateEvent {
    data class CategorySelected(val categoryType: IconType) : CreateEvent()
    data class CardSelectedId(val id: Long = 0, val isTopCard: Boolean = false) : CreateEvent()
    data class AmountChanged(val lastAmount: String, val newNumber: String) : CreateEvent()
    data object ReverseCards : CreateEvent()
    data object SaveData : CreateEvent()
}