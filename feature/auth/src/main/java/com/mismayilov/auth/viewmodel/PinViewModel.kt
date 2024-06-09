package com.mismayilov.auth.viewmodel

import com.mismayilov.auth.flow.pin.PinEffect
import com.mismayilov.auth.flow.pin.PinEvent
import com.mismayilov.auth.flow.pin.PinState
import com.mismayilov.auth.fragment.PinFragment.Companion.isLockedScreen
import com.mismayilov.auth.fragment.PinFragment.Companion.twoFactorAuth
import com.mismayilov.core.base.viewmodel.BaseViewModel
import com.mismayilov.data.local.SharedPreferencesManager
import javax.inject.Inject

class PinViewModel @Inject constructor() : BaseViewModel<PinState, PinEvent, PinEffect>() {
    override fun getInitialState(): PinState = PinState()

    private val _twoFactorAuthPinCode =
        SharedPreferencesManager.getValue("PennyPlannerPinCode", "159753")
    private var _pinCodeFirst = StringBuilder()
    private var _pinCodeSecond = StringBuilder()
    private var _pinCodeLength = 0

    override fun onEventChanged(event: PinEvent) {
        when (event) {
            is PinEvent.NumberClicked -> setPin(event.number)
            is PinEvent.DeleteClicked -> deletePin()
        }
    }

    private fun deletePin() {
        if (_pinCodeLength == 0) return
        when {
            _pinCodeSecond.isNotEmpty() -> _pinCodeSecond.deleteCharAt(_pinCodeSecond.lastIndex)
            _pinCodeFirst.isNotEmpty() -> _pinCodeFirst.deleteCharAt(_pinCodeFirst.lastIndex)
        }
        _pinCodeLength--
        setState(getCurrentState().copy(pinLength = _pinCodeLength))
    }

    private fun setPin(number: String) {
        if (_pinCodeFirst.length < 4) _pinCodeFirst.append(number)
        else _pinCodeSecond.append(number)
        _pinCodeLength++
        setState(getCurrentState().copy(pinLength = _pinCodeLength))
        checkPin()
    }

    private fun checkPin() {
        if (twoFactorAuth && _pinCodeFirst.length == 4) {
            handleTwoFactorAuth()
        } else if (_pinCodeFirst.length == 4 && _pinCodeSecond.length == 4) {
            handlePinMatch()
        } else if (_pinCodeFirst.length == 4) {
            setState(getCurrentState().copy(pinLength = 0))
        }
    }

    private fun handleTwoFactorAuth() {
        if (_pinCodeFirst.toString() == _twoFactorAuthPinCode) {
            navigateToAppropriateScreen()
        } else {
            resetPin()
            setEffect(PinEffect.ShowToast("Pin code is incorrect"))
        }
    }

    private fun handlePinMatch() {
        if (_pinCodeFirst.toString() == _pinCodeSecond.toString()) {
            SharedPreferencesManager.setValue("PennyPlannerPinCode", _pinCodeFirst.toString())
            SharedPreferencesManager.setValue("twoFactorAuth", true)
            twoFactorAuth = true
            setEffect(PinEffect.NavigateToHome)
        } else {
            resetPin()
            setEffect(PinEffect.ShowToast("Pin codes do not match"))
        }
    }

    private fun navigateToAppropriateScreen() {
        if (isLockedScreen) {
            setEffect(PinEffect.NavigateToBack)
        } else {
            setEffect(PinEffect.NavigateToMain)
        }
    }

    private fun resetPin() {
        _pinCodeFirst.clear()
        _pinCodeSecond.clear()
        _pinCodeLength = 0
        setState(getCurrentState().copy(pinLength = 0))
    }

}
