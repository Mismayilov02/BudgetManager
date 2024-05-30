package com.mismayilov.account.flow.create_account

sealed class CreateAccountEvent {
    data class CreateAccount(
        val accountName: String,
        val accountType: String,
        val iconPosition: Int,
        val currency: String,
        val balance: String,
        val isUpdate: Boolean = false
    ) : CreateAccountEvent()

    data class GetAccount(val id: Long) : CreateAccountEvent()
}