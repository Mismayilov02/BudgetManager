package com.mismayilov.create.flow

import com.mismayilov.common.unums.IconType

sealed class CreateEvent {
    data class CategorySelected(val categoryType: IconType) : CreateEvent()
    data class CardSelectedId(val id: Long = 0, val isTopCard: Boolean = false) : CreateEvent()
    data class AmountChanged(val lastAmount: String, val newNumber: String) : CreateEvent()
    data class ClickCard(val isTop: Boolean) : CreateEvent()
    data object ReverseCards : CreateEvent()
    data class SaveData(val date: Long, val note: String?) : CreateEvent()
    data class GetTransaction(val id: Long) : CreateEvent()
}