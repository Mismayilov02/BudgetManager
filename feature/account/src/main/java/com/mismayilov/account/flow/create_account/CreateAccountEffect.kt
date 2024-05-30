package com.mismayilov.account.flow.create_account

sealed class CreateAccountEffect {
    data class ShowToast(val message: String) : CreateAccountEffect()
    data object CloseFragment : CreateAccountEffect()
}