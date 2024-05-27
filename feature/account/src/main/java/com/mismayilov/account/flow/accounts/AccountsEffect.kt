package com.mismayilov.account.flow.accounts

sealed class AccountsEffect {
    data class ShowError(val message: String) : AccountsEffect()
}