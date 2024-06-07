package com.mismayilov.auth.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mismayilov.auth.databinding.FragmentOnboardingBinding
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.data.local.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var binding: FragmentOnboardingBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnGetStarted?.setOnClickListener {
            SharedPreferencesManager.setValue("isUserLoggedIn", true)
            (activity as NavigationManager).navigateToMain()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}