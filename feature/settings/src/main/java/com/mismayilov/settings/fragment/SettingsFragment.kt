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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return _binding?.root
    }
    @Inject
    lateinit var reminderManager: ReminderManager

    private val simpleDateFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initReminder()
        initClickListeners()
    }

    private fun initReminder() {
        _binding?.apply {
            reminderItemView.setChecked = SharedPreferencesManager.getValue("reminder", false)
            reminderItemView.setTimerText(
                simpleDateFormat.format(SharedPreferencesManager.getValue("reminderTime", 0L))
            )
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
            reminderItemView.setSwitchListener = { isChecked, textView ->
                getTimePicker(isChecked, textView)

            }
        }
    }

    private fun getTimePicker(checked: Boolean, textView: TextView) {
        if (checked) {
            showTimePicker(requireContext(), onTimeSelected = {
                SharedPreferencesManager.setValue("reminderTime", it)
                SharedPreferencesManager.setValue("reminder", true)
                textView.text = simpleDateFormat.format(it)
                textView.visibility = View.VISIBLE
                val hours = simpleDateFormat.format(it).split(":")[0].toInt()
                val minutes = simpleDateFormat.format(it).split(":")[1].toInt()
                reminderManager.scheduleDailyReminder(hours, minutes)
            }, onCancel = {
                _binding?.reminderItemView?.setChecked = false
            })
        } else {
            reminderManager.cancelDailyReminder()
            SharedPreferencesManager.setValue("reminder", false)
            textView.visibility = View.GONE
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}