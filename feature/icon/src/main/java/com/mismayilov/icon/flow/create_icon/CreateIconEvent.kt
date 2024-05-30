package com.mismayilov.icon.flow.create_icon


sealed class CreateIconEvent {
    data class AddIconClicked(
        val name: String,
        val iconPosition: Int,
        val iconType: String,
        val isEdit: Boolean,
        val id: Long? = null
    ) : CreateIconEvent()
}