package com.mismayilov.settings.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.mismayilov.common.utility.Util.Companion.TIME_FORMAT
import com.mismayilov.common.utility.showTimePicker
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.core.managers.TwoFactorAuthManager
import com.mismayilov.data.local.SharedPreferencesManager
import com.mismayilov.manager.ReminderManager
import com.mismayilov.settings.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val simpleDateFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())

    @Inject
    lateinit var reminderManager: ReminderManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return  _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initReminder()
        initClickListeners()
        initBalanceSwitch()
        initTwoFactorAuth()
    }

    private fun initTwoFactorAuth() {
         _binding!!.otpSwitch.isChecked = SharedPreferencesManager.getValue("twoFactorAuth", false)
         _binding!!.otpSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                (activity as NavigationManager).navigateByNavigationName("pinFragment")
            } else {
                (activity as TwoFactorAuthManager).setTwoFactorAuthEnabled(false)
            }
        }
    }

    private fun initBalanceSwitch() {
        val showBalance = SharedPreferencesManager.getValue("hideBalance", false)
         _binding!!.balanceSwitch.isChecked = showBalance
         _binding!!.balanceSwitch.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesManager.setValue("hideBalance", isChecked)
        }
    }

    private fun initReminder() {
        _binding?.apply {
            reminderSwitch.isChecked = SharedPreferencesManager.getValue("reminder", false)
            reminderSubText.text = simpleDateFormat.format(SharedPreferencesManager.getValue("reminderTime", 0L))
            reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
                handleReminderSwitch(isChecked, reminderSubText)
            }
        }
    }

    private fun handleReminderSwitch(isChecked: Boolean, textView: TextView) {
        if (isChecked) {
            showTimePicker(requireContext(), onTimeSelected = {
                SharedPreferencesManager.setValue("reminderTime", it)
                SharedPreferencesManager.setValue("reminder", true)
                textView.text = simpleDateFormat.format(it)
                textView.isVisible = true
                reminderManager.scheduleDailyReminder(it)
            }, onCancel = {
                 _binding!!.reminderSwitch.isChecked = false
            })
        } else {
            reminderManager.cancelDailyReminder()
            SharedPreferencesManager.setValue("reminder", false)
            textView.isVisible = false
        }
    }

    private fun initClickListeners() {
        _binding?.apply {
            accountItemView.setOnClickListener {
                (activity as NavigationManager).navigateByNavigationName("account_navigation")
            }
            iconItemView.setOnClickListener {
                (activity as NavigationManager).navigateByNavigationName("icon_navigation")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
         _binding = null
    }
}