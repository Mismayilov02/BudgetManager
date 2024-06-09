package com.mismayilov.auth.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.mismayilov.auth.R
import com.mismayilov.auth.databinding.FragmentSplashScreenBinding
import com.mismayilov.auth.fragment.PinFragment.Companion.twoFactorAuth
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.data.local.SharedPreferencesManager

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isUserLoggedIn = SharedPreferencesManager.getValue("isUserLoggedIn", false)

        view.postDelayed({
            when {
                isUserLoggedIn && twoFactorAuth -> {
                    (activity as NavigationManager).navigateByNavigationName(
                        "pinFragment",
                        "splashScreenFragment"
                    )
                }
                isUserLoggedIn -> (activity as NavigationManager).navigateToMain()
                else -> (activity as NavigationManager).navigateByNavigationName(
                    "onboardingFragment",
                    "splashScreenFragment"
                )
            }
        }, 2200)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}