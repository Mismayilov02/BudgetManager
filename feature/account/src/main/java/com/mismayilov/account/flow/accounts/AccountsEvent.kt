package com.mismayilov.account.flow.accounts

sealed class AccountsEvent{
    data class DeleteAccount(val id: Long) : AccountsEvent()
}