package com.mismayilov.core.managers

interface TwoFactorAuthManager {
    fun isTwoFactorAuthEnabled(): Boolean
    fun setTwoFactorAuthEnabled(isEnabled: Boolean)
}