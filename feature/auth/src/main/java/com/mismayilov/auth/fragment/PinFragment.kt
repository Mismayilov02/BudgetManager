package com.mismayilov.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mismayilov.auth.databinding.FragmentPinBinding
import com.mismayilov.auth.flow.pin.PinEffect
import com.mismayilov.auth.flow.pin.PinEvent
import com.mismayilov.auth.flow.pin.PinState
import com.mismayilov.auth.viewmodel.PinViewModel
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.data.local.SharedPreferencesManager
import com.mismayilov.uikit.util.showToast

class PinFragment : BaseFragment<FragmentPinBinding,PinViewModel,PinState,PinEvent,PinEffect>() {
    override fun getViewModelClass(): Class<PinViewModel> = PinViewModel::class.java
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPinBinding={
        inflater,container,attachToRoot->FragmentPinBinding.inflate(inflater,container,attachToRoot)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NavigationManager)?.bottomNavigationVisibility(false)
        initNumberPad()
    }

    private fun initNumberPad() {
        binding.apply {
           val numbers = listOf(btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9)
            numbers.forEachIndexed { _, button ->
                button.setOnClickListener {
                    setEvent(PinEvent.NumberClicked(button.text.toString()))
                }
            }
            btnDelete.setOnClickListener {
                setEvent(PinEvent.DeleteClicked)
            }
            btnX.setOnClickListener {
                if(!isLockedScreen) {
                    (activity as? NavigationManager)?.navigateByNavigationName("home_navigation","home_navigation")
                }else{
                    showToast("Please enter your pin")
                }
            }
        }
    }

    override fun renderState(state: PinState) {
        binding.apply {
            radioButton1.isChecked = state.pinLength > 0
            radioButton2.isChecked = state.pinLength > 1
            radioButton3.isChecked = state.pinLength > 2
            radioButton4.isChecked = state.pinLength > 3
        }
    }

    override fun renderEffect(effect: PinEffect) {
        when(effect){
            is PinEffect.ShowToast->showToast(effect.message)
            is PinEffect.NavigateToMain->(activity as? NavigationManager)?.navigateToMain()
            is PinEffect.NavigateToHome-> {
                isLockedScreen = false
                (activity as? NavigationManager)?.navigateByNavigationName("home_navigation","home_navigation")
            }
            is PinEffect.NavigateToBack->(activity as? NavigationManager)?.back()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? NavigationManager)?.bottomNavigationVisibility(true)
        isLockedScreen = false
    }

    companion object {
        var isLockedScreen = false
        var twoFactorAuth = false
    }

}